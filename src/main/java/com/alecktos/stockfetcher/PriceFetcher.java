package com.alecktos.stockfetcher;

import com.alecktos.misc.LineFileReader;
import com.alecktos.stockfetcher.markitondemand.MarkItOnDemand;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PriceFetcher {

	@Inject
	private MarkItOnDemand markItOnDemand;

	@Inject
	private LineFileReader lineFileReader;

	public double fetch(String filePath) {
		double price = markItOnDemand.getCurrentStockPrice();
		if(price == 0.0) {
			return getLastSavedPrice(filePath);
		}
		return price;
	}

	private double getLastSavedPrice(String filePath) {
		lineFileReader.getLastLine(filePath);
		StockFileLineExtractor stockFileLineExtractor = new StockFileLineExtractor(filePath);
		return stockFileLineExtractor.getPriceFromRow();
	}
}
