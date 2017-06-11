package com.alecktos.stockfetcher;

import com.alecktos.DateTime;
import com.google.inject.Inject;
import com.alecktos.logger.Logger;
import com.alecktos.stockfetcher.markitondemand.MarkItOnDemand;
import com.alecktos.stocklibrary.OpenStockHandler;
import com.alecktos.stocklibrary.configs.DisneyAvanzaConfig;

class StockfetcherRunner {

	@Inject
	private Logger logger;

	@Inject
	private LastLineFileRepairer lastLineFileRepairer;

	void run(DateTime dateTime, DisneyAvanzaConfig disneyAvanzaConfig) {
		try {
			saveCurrentPrice(dateTime, disneyAvanzaConfig);
		} catch(RuntimeException e) {
			logger.logAndAlert("Archive: Exception during run: " + e.toString(), Main.class);

			repairFile(disneyAvanzaConfig, dateTime);

			logger.logAndAlert("repaired file", Main.class);
		}
	}

	private void saveCurrentPrice(DateTime dateTime, DisneyAvanzaConfig disneyAvanzaConfig) {
		final OpenStockHandler openStockHandler = new OpenStockHandler(); //TODO inject
		if(!openStockHandler.isOpen(dateTime, disneyAvanzaConfig.getOpeningTime(), disneyAvanzaConfig.getClosingTime())) {
			logger.log("Stock is not open. Will not save to file. " + disneyAvanzaConfig.getArchivePath(), StockfetcherRunner.class);
			return;
		}

		MarkItOnDemand markItOnDemand = new MarkItOnDemand();
		double price = markItOnDemand.getCurrentStockPrice();
		PriceFileSaver priceFileSaver = new PriceFileSaver();
		priceFileSaver.savePrice(disneyAvanzaConfig.getArchivePath(), price, dateTime);
	}

	private void repairFile(DisneyAvanzaConfig disneyAvanzaConfig, DateTime dateTime) {
		lastLineFileRepairer.repairLastLine(disneyAvanzaConfig, dateTime, DateTime.createFromNow());
	}

}
