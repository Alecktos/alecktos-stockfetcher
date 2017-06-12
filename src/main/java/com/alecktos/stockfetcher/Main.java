package com.alecktos.stockfetcher;


import com.alecktos.logger.Logger;
import com.alecktos.marketopen.DateTime;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class Main {

	public static void main(String[] args) {
		String mode = args.length > 0 ? args[0] : "emulation";
		String filePath = args[1];
		String openingTime = args[2];
		String closingTime = args[3];

		Logger.doAlert("Stockfetcher: " + mode);

		Module module = new StockfetcherDependencyModule() {
		};

		Injector injector = Guice.createInjector(module);

		final StockfetcherRunner stockfetcherRunner = injector.getInstance(StockfetcherRunner.class);
		stockfetcherRunner.run(DateTime.createFromNow(), openingTime, closingTime, filePath);
	}

}
