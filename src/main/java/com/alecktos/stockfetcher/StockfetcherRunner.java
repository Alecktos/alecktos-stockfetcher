package com.alecktos.stockfetcher;


import com.alecktos.misc.logger.Logger;
import com.alecktos.marketopen.DateTime;
import com.alecktos.marketopen.OpenStockHandler;
import com.alecktos.stockfetcher.markitondemand.MarkItOnDemand;
import com.google.inject.Inject;

class StockfetcherRunner {

	@Inject
	private Logger logger;

	@Inject
	private LastLineFileRepairer lastLineFileRepairer;

	@Inject
	private OpenStockHandler openStockHandler;

	void run(DateTime dateTime, String openingTime, String closingTime, String filePath) {
		try {
			saveCurrentPrice(dateTime, openingTime, closingTime, filePath);
		} catch(RuntimeException e) {
			logger.logAndAlert("Archive: Exception during run: " + e.toString(), Main.class);

			repairFile(filePath, dateTime);

			logger.logAndAlert("repaired file", Main.class);
		}
	}

	private void saveCurrentPrice(DateTime dateTime, String openingTime, String closingTime, String filePath) {
		if(!openStockHandler.isOpen(dateTime, openingTime, closingTime)) {
			logger.log("Stock is not open. Will not save to file. " + filePath, StockfetcherRunner.class);
			return;
		}

		MarkItOnDemand markItOnDemand = new MarkItOnDemand();
		double price = markItOnDemand.getCurrentStockPrice();
		PriceFileSaver priceFileSaver = new PriceFileSaver();
		priceFileSaver.savePrice(filePath, price, dateTime);
	}

	private void repairFile(String filePath, DateTime dateTime) {
		lastLineFileRepairer.repairLastLine(filePath, dateTime, DateTime.createFromNow());
	}

}
