package com.alecktos.stockfetcher;

import com.alecktos.misc.FileHandler;
import com.alecktos.misc.logger.Logger;
import com.alecktos.marketopen.DateTime;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.testutils.FileExist;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.mock;

public class StockfetcherRunnerTest {

	private static String disneyStockTestPath = "src/test/disneyStockTest.txt";
	private StockfetcherRunner stockfetcherRunner;

	public StockfetcherRunnerTest() {
		Logger logger = mock(Logger.class);
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(Logger.class).toInstance(logger);
			}
		};
		Injector injector = Guice.createInjector(module);
		stockfetcherRunner = injector.getInstance(StockfetcherRunner.class);
	}

	private static void removeTestFile() {
		try {
			FileHandler.deleteFile(disneyStockTestPath);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Before
	public void before() {
		removeTestFile();
	}

	@AfterClass
	public static void cleanUpAfterAllTestsRun() {
		removeTestFile();
	}

	@Test
	public void testFilePriceSaverShouldNotSaveOnColumbusDay() {
		DateTime dateTime = DateTime.createFromDateTimeString("10/10/2016 16:10:51");

		stockfetcherRunner.run(dateTime, "14:30:00", "21:00:00", disneyStockTestPath);

		Path path = Paths.get(disneyStockTestPath);
		Assert.assertFalse(Files.exists(path));
	}

	@Test
	public void testNotSavingPriceWhenNotOpen() {
		DateTime dateTime = DateTime.createFromDateTimeString("01/16/2017 16:10:51");

		stockfetcherRunner.run(dateTime, "14:30:00", "21:00:00", disneyStockTestPath);

		assertFalse(FileExist.doExist(disneyStockTestPath));
	}

	@Test
	public void testSavingPriceIfOpen() {
		DateTime dateTime = DateTime.createFromDateTimeString("01/18/2017 16:10:51");

		stockfetcherRunner.run(dateTime, "14:30:00", "21:00:00", disneyStockTestPath);

		assertTrue(FileExist.doExist(disneyStockTestPath));
	}

}
