package com.alecktos.stockfetcher;

import com.alecktos.misc.FileHandler;
import com.alecktos.misc.InfluxdbDAO;
import com.alecktos.misc.logger.Logger;
import com.alecktos.misc.DateTime;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.io.IOException;
import java.io.PrintWriter;

class PriceFileSaver {

	private Logger logger;
	private InfluxdbDAO influxdbDAO;
	private String influxDbName;

	@Inject
		public PriceFileSaver(Logger logger, InfluxdbDAO influxdbDAO, @Named("influxDbName") final String influxDbName) {
		this.logger = logger;
		this.influxdbDAO= influxdbDAO;
		this.influxDbName = influxDbName;
	}

	void savePrice(String filePath, double price, DateTime dateTime) {
		saveToFile(filePath, price, dateTime);
		influxdbDAO.writeInflux(price, dateTime, influxDbName);
	}

	private void saveToFile(String filePath, double price, DateTime dateTime) {
		try (PrintWriter printWriter = FileHandler.getFileWriter(filePath)) { //By putting it in a try statement I ensure that it is always closed (java 7 and above feature).
			createFileWithMetaIfFileNotExist(filePath, printWriter);
			printWriter.println(Double.toString(price) + ':' + dateTime.toTimeStamp() + " # " + dateTime.toString());
		} catch (IOException e) {
			throw new RuntimeException("Failed saving price to file: " + e.getMessage() + e.getStackTrace().toString());
		}
	}

	private void createFileWithMetaIfFileNotExist(String filePath, PrintWriter printWriter) {
		try {
			if (FileHandler.getFileReader(filePath).readLine() == null) {
				printWriter.println("content-interval:5m  open:14.30-21.00 #time is in UTC");
			}
		} catch (IOException e) {
			logger.logAndAlert("Failed creating file with meta", getClass());
			e.printStackTrace();
		}
	}

}
