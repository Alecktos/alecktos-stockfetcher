package com.alecktos.stockfetcher;


import com.alecktos.misc.logger.Logger;
import com.alecktos.misc.DateTime;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class Main {

	public static void main(String[] args) {
		String mode = args[0];
		String stockFilePath = args[1];
		String emailConfigPath = args[2];
		String openingTime = args[3];
		String closingTime = args[4];

		Logger.doAlert("Stockfetcher: " + mode);

		Module module = new StockfetcherDependencyModule(emailConfigPath) {
		};

		Injector injector = Guice.createInjector(module);

		final StockfetcherRunner stockfetcherRunner = injector.getInstance(StockfetcherRunner.class);
		stockfetcherRunner.run(DateTime.createFromNow(), openingTime, closingTime, stockFilePath);
	}

}
