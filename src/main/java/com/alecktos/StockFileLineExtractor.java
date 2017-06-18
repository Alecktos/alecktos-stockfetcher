package com.alecktos;

import com.alecktos.misc.DateTime;

public class StockFileLineExtractor {

	private final String[] splittedLine;

	public StockFileLineExtractor(String line) {
		line = line.split("#")[0]; //remove comments
		line = line.trim();
		this.splittedLine = line.split(":");
	}

	public double getPriceFromRow() {
		return Double.valueOf(splittedLine[0]);
	}

	public String getDateTimeStringFromRow() {
		Long timeStamp = getTimeStamp();
		return DateTime.createFromTimeStamp(timeStamp).toString();
	}

	public Long getTimeStamp() {
		return Long.valueOf(splittedLine[1]);
	}

}
