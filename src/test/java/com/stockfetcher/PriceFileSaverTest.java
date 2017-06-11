package com.stockfetcher;

import com.DateTime;
import com.FileHandler;
import com.stockfetcher.markitondemand.MarkItOnDemand;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class PriceFileSaverTest {

	private static String disneyStockTestPath = "src/test/disneyStockTest.txt";

	private static void removeTestFile() {
		try {
			FileHandler.deleteFile(disneyStockTestPath);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Before
	public void cleanUp() {
		removeTestFile();
	}

	@Test
	public void testFilePriceSaverShouldSave() {
		PriceFileSaver filePriceSaver = new PriceFileSaver();
		DateTime dateTime = DateTime.createFromDateTimeString("10/11/2016 16:10:51");
		filePriceSaver.savePrice(disneyStockTestPath, 12, dateTime);

		try {
			final BufferedReader fileReader = FileHandler.getFileReader(disneyStockTestPath);
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
		assertFalse(disneyStockFileExist());
		MarkItOnDemand markItOnDemand = new MarkItOnDemand();
		Double currentStockPrice = markItOnDemand.getCurrentStockPrice();

		DateTime dateTime = DateTime.createFromDateTimeString("16/11/2016 14:44:00");

		PriceFileSaver priceFileSaver = new PriceFileSaver();
		priceFileSaver.savePrice(disneyStockTestPath, currentStockPrice, dateTime);

		assertTrue(disneyStockFileExist());
	}

	private boolean disneyStockFileExist() {
		File f = new File(disneyStockTestPath);
		return f.exists() && !f.isDirectory();
	}
}
