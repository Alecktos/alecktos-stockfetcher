package com.alecktos.stockfetcher;

import com.alecktos.misc.DateTime;
import com.alecktos.misc.FileHandler;
import com.alecktos.stockfetcher.markitondemand.MarkItOnDemand;
import com.testutils.TestStockFile;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class PriceFileSaverTest {

	@Before
	public void cleanUp() {
		TestStockFile.removeFile();
	}

	@Test
	public void testFilePriceSaverShouldSave() {
		PriceFileSaver filePriceSaver = new PriceFileSaver();
		DateTime dateTime = DateTime.createFromDateTimeString("10/11/2016 16:10:51");
		filePriceSaver.savePrice(TestStockFile.getFilePath(), 12, dateTime);

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

		PriceFileSaver priceFileSaver = new PriceFileSaver();
		priceFileSaver.savePrice(TestStockFile.getFilePath(), currentStockPrice, dateTime);

		assertTrue(TestStockFile.fileExist());
	}
}
