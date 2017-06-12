package com.alecktos.stockfetcher;

import com.alecktos.FileHandler;
import com.alecktos.LineFileReader;
import com.alecktos.logger.Logger;
import com.alecktos.marketopen.DateTime;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class LastLineFileRepairerTest {

	private static String disneyStockTestPath = "src/test/disneyStockTest.txt";

	private static void removeTestFile() {
		try {
			Files.deleteIfExists(FileSystems.getDefault().getPath(disneyStockTestPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void before() {
		removeTestFile();
		try {
			final PrintWriter printWriter = FileHandler.getFileWriter(disneyStockTestPath);

			ArrayList<String> lines = new ArrayList<>();
			lines.add("91.8:1473949804000 # 09/15/2016 14:30:04");
			lines.add("91.83:1473951004000 # 09/15/2016 14:50:04");
			lines.add("92.12:1473951904000 # 09/15/2016 15:05:04");
			lines.add("92.09:1473952804000 # 09/15/2016 15:20:04");
			lines.add("92.1:1473953104000 # 09/15/2016 15:25:04");
			lines.add("92.23:1473955503000 # 09/15/2016 16:05:03");
			lines.add("92.22:1473956404000 # 09/15/2016 16:20:04");
			lines.add("92.17:1473957004000 # 09/15/2016 16:30:04");
			lines.add("92.13:1473957304000 # 09/15/2016 16:35:04");
			lines.add("91.92:1473958503000 # 09/15/2016 16:55:03");
			lines.add("92.005:1473959103000 # 09/15/2016 17:05:03");
			lines.add("92.0:1473960303000 # 09/15/2016 17:25:03");
			lines.add("91.94:1473960604000 # 09/15/2016 17:30:04");
			lines.add("92.05:1473961203000 # 09/15/2016 17:40:03");
			lines.add("92.15:1473961504000 # 09/15/2016 17:45:04");
			lines.add("92.35:1473961804000 # 09/15/2016 17:50:04");
			lines.add("92.41:1473962103000 # 09/15/2016 17:55:03");
			lines.add("92.53:1473964503000 # 09/15/2016 18:35:03");
			lines.add("92.68:1473966004000 # 09/15/2016 19:00:04");
			lines.add("92.71:1473966303000 # 09/15/2016 19:05:03");
			lines.add("92.68:1473966603000 # 09/15/2016 19:10:03");
			lines.add("92.63:1473966904000 # 09/15/2016 19:15:04");
			lines.add("92.525:1473969603000 # 09/15/2016 20:00:03");
			lines.add("92.525:1473969904000 # 09/15/2016 20:05:04");
			lines.add("92.525:1473970204000 # 09/15/2016 20:10:04");
			lines.add("92.525:1473970504000 # 09/15/2016 20:15:04");
			lines.add("92.518:1473970803000 # 09/15/2016 20:20:03");
			lines.add("92.519:1473971104000 # 09/15/2016 20:25:04");
			lines.add("92.520:1473971403000 # 09/15/2016 20:30:03");
			lines.add("92.521:1473971704000 # 09/15/2016 20:35:04");
			lines.add("92.522:1473972004000 # 09/15/2016 20:40:04");
			lines.add("92.523:1473972304000 # 09/15/2016 20:45:04");
			lines.add("92.524:1473972603000 # 09/15/2016 20:50:03");
			lines.add("92.525:1473972903000 # 09/15/2016 20:55:03");

			lines.forEach(printWriter::println);

			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void after() {
		removeTestFile();
	}

	@Test
	public void testThatFileGetsRepaired() {
		Logger logger = mock(Logger.class);

		Module module = new AbstractModule() {
			@Override
			protected void configure() {
				bind(Logger.class).toInstance(logger);
			}
		};

		Injector injector = Guice.createInjector(module);
		LastLineFileRepairer lastLineFileRepairer = injector.getInstance(LastLineFileRepairer.class);

		DateTime dateTimeToSave = DateTime.createFromDateTimeString("09/15/2016 21:00:03");
		//Config config = MockConfig.createMockConfigOpenMarketFromDateTime(disneyStockTestPath, dateTimeToSave);

		lastLineFileRepairer.repairLastLine(disneyStockTestPath, dateTimeToSave, DateTime.createFromNow());

		LineFileReader lineFileReader = new LineFileReader();
		final List<String> lines = lineFileReader.getLinesFromFile(disneyStockTestPath, -1);
		String lastLine =  lines.get(lines.size() - 1);
		assertEquals("92.525:" + dateTimeToSave.toTimeStamp() + " # " + dateTimeToSave.toString(), lastLine);
	}

	@Test
	public void testThatLastLineIsNotRepairedIfAlreadyInsertedCorrect() {
		Logger logger = mock(Logger.class);
		LastLineFileRepairer lastLineFileRepairer = new LastLineFileRepairer(logger);

		DateTime dateTimeToSave = DateTime.createFromDateTimeString("09/15/2016 20:56:01");
		DateTime fakeNow = DateTime.createFromDateTimeString("09/15/2016 20:59:02"); //less then five minutes after last line. This mean that the las line was inserted correct.
		//Config config = MockConfig.createMockConfigOpenMarketFromDateTime(disneyStockTestPath, fakeNow);

		lastLineFileRepairer.repairLastLine(disneyStockTestPath, dateTimeToSave, fakeNow);

		LineFileReader lineFileReader = new LineFileReader();
		final List<String> lines = lineFileReader.getLinesFromFile(disneyStockTestPath, -1);

		String secondLastLine = lines.get(lines.size() - 2);
		assertEquals("92.524:1473972603000 # 09/15/2016 20:50:03", secondLastLine);

		String lastLine = lines.get(lines.size() - 1);
		assertEquals("92.525:1473972903000 # 09/15/2016 20:55:03", lastLine);
	}
}
