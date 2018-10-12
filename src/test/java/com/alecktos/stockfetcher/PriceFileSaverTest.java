package com.alecktos.stockfetcher;

import com.alecktos.misc.DateTime;
import com.alecktos.misc.FileHandler;
import com.alecktos.misc.InfluxdbDAO;
import com.alecktos.misc.logger.Logger;
import com.alecktos.stockfetcher.markitondemand.MarkItOnDemand;
import com.testutils.TestStockFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.Assert.*;

public class PriceFileSaverTest {

	@Before
	public void cleanUp() {
		InfluxdbDAO.deleteDb("stocks_test");
		TestStockFile.removeFile();
	}

	@Test
	public void testFilePriceSaverShouldSave() {
		Logger logger = new Logger((message, subject) -> {});
		PriceFileSaver priceFileSaver = new PriceFileSaver(logger, new InfluxdbDAO(), "stocks_test");

		DateTime dateTime = DateTime.createFromDateTimeString("10/11/2016 16:10:51");
		priceFileSaver.savePrice(TestStockFile.getFilePath(), 12, dateTime);

		try {
			final BufferedReader fileReader = FileHandler.getFileReader(TestStockFile.getFilePath());
			fileReader.readLine();
			String secondLine = fileReader.readLine(); //12.0:1476014811000 # 10/09/2016 12:06:51
			final String[] splits = secondLine.split(":");
			assertEquals("12.0", splits[0]);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testSavingCurrentPriceToFile() {
		assertFalse(TestStockFile.fileExist());
		MarkItOnDemand markItOnDemand = new MarkItOnDemand();
		Double currentStockPrice = markItOnDemand.getCurrentStockPrice();

		DateTime dateTime = DateTime.createFromDateTimeString("16/11/2016 14:44:00");

		Logger logger = new Logger((message, subject) -> {});
		PriceFileSaver priceFileSaver = new PriceFileSaver(logger, new InfluxdbDAO(), "stocks_test");
		priceFileSaver.savePrice(TestStockFile.getFilePath(), currentStockPrice, dateTime);

		assertTrue(TestStockFile.fileExist());

		String q = "SELECT stock_value FROM disney_stock ORDER BY time DESC LIMIT 1";
		try {
			q = URLEncoder.encode(q, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			final String json = InfluxdbDAO.executeGet("http://localhost:8086/query?db=stocks_test&pretty=true&q=" + q);
			JSONObject jsonObject = new JSONObject(json);
			final JSONArray values = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("series").getJSONObject(0).getJSONArray("values");
			assertEquals(values.length(), 1);
			assertEquals(values.getJSONArray(0).get(0), "2017-04-11T14:44:00Z");
			assertEquals(values.getJSONArray(0).get(1), currentStockPrice);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
