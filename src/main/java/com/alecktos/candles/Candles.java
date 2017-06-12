package com.alecktos.candles;

import com.alecktos.StockFileLineExtractor;
import com.alecktos.logger.Logger;
import com.alecktos.marketopen.DateTime;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Candles implements Iterable<CandleStick> {

	private List<CandleStick> candleSticks = new ArrayList<>();
	private double firstPrice;
	private String firstDateTime;
	private double lastPrice;
	private String lastDateTime;
	private Logger logger;

	public Candles(final List<CandleStick> candleList, final Logger logger) {
		candleSticks = candleList;
		if(candleList.size() < 1) {
			return;
		}

		final CandleStick firstCandle = candleSticks.get(0);
		final CandleStick lastCandle = candleSticks.get(candleSticks.size() - 1);

		firstPrice = firstCandle.getOpenPrice().doubleValue();
		firstDateTime = firstCandle.getOpenDateTimeString();
		lastPrice = lastCandle.getClosePrice().doubleValue();
		lastDateTime = lastCandle.getCloseDateTimeString();
		this.logger = logger;
	}

	public Candles(final List<String> lines, int minutes, final Logger logger) {
		if(lines.size() < (minutes / 5) + 1 + 1) { //last plus 1 because first line is metadata
			throw new RuntimeException(String.format("Not enough lines in file. Line size is %d: should be at least: %f. Minutes is set too %d", lines.size(), (minutes / 5.0) + 1.0 + 1.0, minutes));
		}

		String contentIntervalLine = lines.remove(0);
		int rowPicker = getRowPicker(minutes, contentIntervalLine);

		StockFileLineExtractor firstLine = new StockFileLineExtractor(lines.get(0));
		StockFileLineExtractor lastLine = new StockFileLineExtractor(lines.get(lines.size() - 1));

		//if equally divided with rowpicker we will not get the last row.
		while(!numberOfRowWorksWithDatePicker(rowPicker, lines.size())) {
			lines.remove(0);
		}

		String oldLine = lines.remove(0);
		String line = getNextRow(lines, rowPicker);

		while (line != null) {
			CandleStick candle = getCandleFromPriceLines(line, oldLine);
			//logCandleStickInfo(candle);
			candleSticks.add(candle);
			oldLine = line;
			line = getNextRow(lines, rowPicker);
		}

		firstPrice = firstLine.getPriceFromRow();
		firstDateTime = firstLine.getDateTimeStringFromRow();
		lastPrice = lastLine.getPriceFromRow();
		lastDateTime = lastLine.getDateTimeStringFromRow();
		this.logger = logger;
	}

	public double getFirstPrice() {
		return firstPrice;
	}

	public double getLastPrice() {
		return lastPrice;
	}

	public String getLastDateTimeString() {
		return lastDateTime;
	}

	/**
	 * Includes none business days
	 */
	private int numberOfDays() {
		return DateTime.createFromDateTimeString(firstDateTime).daysBetween(lastDateTime);
	}

	public void logStockMovement() {
		logger.log("Stock movement: " +  getStockMovement(), getClass());
		logger.log("Number of days: " + numberOfDays(), getClass());
	}

	public String getStockMovement() {
		return ((1 - firstPrice / lastPrice) * 100) + "%";
	}

	private CandleStick getCandleFromPriceLines(String newLine, String oldLine) {
		StockFileLineExtractor newLineExtractor = new StockFileLineExtractor(newLine);
		StockFileLineExtractor oldLineExtractor = new StockFileLineExtractor(oldLine);

		Long newTimeStamp = newLineExtractor.getTimeStamp();
		Long oldTimeStamp = oldLineExtractor.getTimeStamp();

		CandleStick candleStick = new CandleStick();

		if (newTimeStamp > oldTimeStamp) {
			candleStick.setOpenPrice(oldLineExtractor.getPriceFromRow(), oldLineExtractor.getDateTimeStringFromRow());
			candleStick.setClosePrice(newLineExtractor.getPriceFromRow(), newLineExtractor.getDateTimeStringFromRow());
		} else {
			throw new RuntimeException("CandleStick file has wrong format");
		}
		return candleStick;
	}

	public CandleStick get(int index) {
		return candleSticks.get(index);
	}

	public CandleStick getLast() {
		return candleSticks.get(candleSticks.size() - 1);
	}

	public int size() {
		return candleSticks.size();
	}

	private String getNextRow(final List<String> lines, int rowPicker) {
		if(lines.size() == 0) {
			return null;
		}

		String line = lines.remove(0);
		rowPicker--;
		while(lines.size() > 0 && rowPicker > 0) {
			line = lines.remove(0);
			rowPicker--;
		}

		if(rowPicker > 0) {
			throw new RuntimeException("does not support candlestick time");
		}

		return line;
	}

	private boolean numberOfRowWorksWithDatePicker(final int rowPicker, final int linesLength) {
		if(linesLength - rowPicker > rowPicker) {
			return numberOfRowWorksWithDatePicker(rowPicker, linesLength - rowPicker);
		}

		return (linesLength == rowPicker + 1);
	}

	private int getRowPicker(final int minutes, final String contentIntervalLine) {
		int fileIntervalMinutes = getIntervalInMinutes(contentIntervalLine);
		int rowPicker = 1;
		if(fileIntervalMinutes != minutes) {
			double rowMultiplier =  minutes / (double)fileIntervalMinutes;
			if(rowMultiplier % 1 != 0) {
				throw new RuntimeException("candlestick interval has bee equally divided with zero");
			}
			rowPicker = (int) rowMultiplier;
		}
		return rowPicker;
	}

	private int getIntervalInMinutes(String contentIntervalLine) {
		String[] result = contentIntervalLine.split("content-interval:");
		int indexOfEndSign = result[1].indexOf('m');
		String minutes = result[1].substring(0, indexOfEndSign);
		return Integer.parseInt(minutes);
	}

	private void logCandleStickInfo(CandleStick candleStick) {
		logger.log("-- Candle --", getClass());
		logger.log("Open price: " + candleStick.getOpenPrice() + " time: " + candleStick.getOpenDateTimeString(), getClass());
		logger.log("Close price: " + candleStick.getClosePrice() + " time: " + candleStick.getCloseDateTimeString(), getClass());
		logger.log("-- Candle END --", getClass());
	}

	@Override
	public Iterator<CandleStick> iterator() {
		return candleSticks.iterator();
	}
}
