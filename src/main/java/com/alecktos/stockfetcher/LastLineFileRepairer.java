package com.alecktos.stockfetcher;

import com.alecktos.misc.DateTime;
import com.alecktos.misc.InfluxdbDAO;
import com.alecktos.misc.LineFileReader;
import com.alecktos.misc.logger.AlertNotifierInterface;
import com.alecktos.misc.logger.Logger;
import com.google.inject.Inject;

class LastLineFileRepairer {

	private Logger logger;

	private LineFileReader lineFileReader;

	@Inject
	public LastLineFileRepairer(Logger logger, LineFileReader lineFileReader) {
		this.logger = logger;
		this.lineFileReader = lineFileReader;
	}

	/**
	 * If last line was not added more then 4 minutes ago something went wrong
	 * @param lastLineDateTime
	 * @return
	 */
	private boolean lastLineWasJustAdded(DateTime lastLineDateTime, DateTime now) {
		DateTime dateTimeFrom = now.createDateTimeWithTimeRewind(4);

		return lastLineDateTime.isInInterval(dateTimeFrom, now);
	}

	void repairLastLine(String filePath, DateTime dateTimeToSave, DateTime now) {
		//String filePath = config.getArchivePath();

		String lastLine = lineFileReader.getLastLine(filePath);

		StockFileLineExtractor stockFileLineExtractor = new StockFileLineExtractor(lastLine);
		DateTime lastInsertedPriceDateTime = DateTime.createFromDateTimeString(stockFileLineExtractor.getDateTimeStringFromRow());

		if(lastLineWasJustAdded(lastInsertedPriceDateTime, now)) {
			logger.log("Did not add new line to file. The last when just added (probably correctly)", getClass());
			return;
		}

		Logger logger = new Logger((message, subject) -> {});
		PriceFileSaver priceSaver = new PriceFileSaver(logger, new InfluxdbDAO(), "stocks_test");
		double lastPrice = new StockFileLineExtractor(lastLine).getPriceFromRow();

		priceSaver.savePrice(filePath, lastPrice, dateTimeToSave);
		logger.logAndAlert("Saved price for repaired last line", getClass());
	}

}
