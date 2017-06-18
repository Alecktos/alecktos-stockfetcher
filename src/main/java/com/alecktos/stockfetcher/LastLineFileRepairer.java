package com.alecktos.stockfetcher;

import com.alecktos.misc.LineFileReader;
import com.alecktos.StockFileLineExtractor;
import com.alecktos.misc.logger.Logger;
import com.alecktos.misc.DateTime;
import com.google.inject.Inject;

import java.util.List;

class LastLineFileRepairer {

	private Logger logger;

	@Inject
	public LastLineFileRepairer(Logger logger) {
		this.logger = logger;
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

		LineFileReader lineFileReader = new LineFileReader();
		final List<String> linesFromFile = lineFileReader.getLinesFromFile(filePath, -1);

		int lastIndex = linesFromFile.size() - 1;
		StockFileLineExtractor stockFileLineExtractor = new StockFileLineExtractor(linesFromFile.get(lastIndex));
		DateTime lastInsertedPriceDateTime = DateTime.createFromDateTimeString(stockFileLineExtractor.getDateTimeStringFromRow());

		if(lastLineWasJustAdded(lastInsertedPriceDateTime, now)) {
			logger.log("Did not add new line to file. The last when just added (probably correctly)", getClass());
			return;
		}

		//repair line
		PriceFileSaver priceSaver = new PriceFileSaver();
		double lastPrice = new StockFileLineExtractor(linesFromFile.get(lastIndex)).getPriceFromRow();

		priceSaver.savePrice(filePath, lastPrice, dateTimeToSave);
		logger.logAndAlert("Saved price for repaired last line", getClass());
	}

}
