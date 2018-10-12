package com.alecktos.stockfetcher;

import com.alecktos.misc.DateTime;
import com.alecktos.misc.InfluxdbDAO;
import com.alecktos.misc.LineFileReader;
import com.alecktos.misc.logger.Logger;
import com.alecktos.stockfetcher.markitondemand.MarkItOnDemand;
import com.google.inject.*;
import com.google.inject.name.Names;
import com.testutils.FileExist;
import com.testutils.TestStockFile;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StockfetcherRunnerTest {

	private StockfetcherRunner stockfetcherRunner;

	public StockfetcherRunnerTest() {
		Logger logger = mock(Logger.class);
		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(Logger.class).toInstance(logger);
				bindConstant().annotatedWith(Names.named("influxDbName")).to("db_test");
			}
		};
		Injector injector = Guice.createInjector(module);
		stockfetcherRunner = injector.getInstance(StockfetcherRunner.class);
	}

	@Before
	public void before() {
		TestStockFile.removeFile();
		InfluxdbDAO.deleteDb("db_test");
	}

	@AfterClass
	public static void cleanUpAfterAllTestsRun() {
		TestStockFile.removeFile();
	}

	@Test
	public void testFilePriceSaverShouldNotSaveOnColumbusDay() {
		DateTime dateTime = DateTime.createFromDateTimeString("10/10/2016 16:10:51");

		stockfetcherRunner.run(dateTime, "14:30:00", "21:00:00", TestStockFile.getFilePath());

		Path path = Paths.get(TestStockFile.getFilePath());
		Assert.assertFalse(Files.exists(path));
	}

	@Test
	public void testNotSavingPriceWhenNotOpen() {
		DateTime dateTime = DateTime.createFromDateTimeString("01/16/2017 16:10:51");

		stockfetcherRunner.run(dateTime, "14:30:00", "21:00:00", TestStockFile.getFilePath());

		assertFalse(FileExist.doExist(TestStockFile.getFilePath()));
	}

	@Test
	public void testSavingPriceIfOpen() {
		DateTime dateTime = DateTime.createFromDateTimeString("01/18/2017 16:10:51");

		stockfetcherRunner.run(dateTime, "14:30:00", "21:00:00", TestStockFile.getFilePath());

		assertTrue(FileExist.doExist(TestStockFile.getFilePath()));
	}

	@Test
	public void testNotSavingWeirdResponse() {
		double price1 = 109.22;
		double price2 = 0.0;

		final MarkItOnDemand markItOnDemandMock = mock(MarkItOnDemand.class);
		when(markItOnDemandMock.getCurrentStockPrice())
				.thenReturn(price1, price2);

		Logger logger = mock(Logger.class);

		Module module = new StockfetcherDependencyModule("should_not_care.yml") {

			@Override
			protected Provider<Logger> getLoggerProvider() {
				return () -> logger;
			}

			@Override
			protected void configure() {
				super.configure();
				bind(MarkItOnDemand.class).toInstance(markItOnDemandMock);
			}
		};

		Injector injector = Guice.createInjector(module);
		final StockfetcherRunner stockfetcherRunner = injector.getInstance(StockfetcherRunner.class);

		assertFalse(TestStockFile.fileExist());

		DateTime dateTime = DateTime.createFromDateTimeString("16/11/2016 14:44:00");
		stockfetcherRunner.run(dateTime, "14:30:00", "21:00:00", TestStockFile.getFilePath());
		assertTrue(TestStockFile.fileExist());


		stockfetcherRunner.run(dateTime, "14:30:00", "21:00:00", TestStockFile.getFilePath());

		//read and assert lines from stock file
		LineFileReader lineFileReader = new LineFileReader();
		final List<String> linesFromFile = lineFileReader.getLinesFromFile(TestStockFile.getFilePath());
		StockFileLineExtractor fileLineExtractor = new StockFileLineExtractor(linesFromFile.get(1));
		assertEquals(price1, fileLineExtractor.getPriceFromRow());

		fileLineExtractor = new StockFileLineExtractor(linesFromFile.get(2));
		assertEquals(price1, fileLineExtractor.getPriceFromRow());
	}

}
