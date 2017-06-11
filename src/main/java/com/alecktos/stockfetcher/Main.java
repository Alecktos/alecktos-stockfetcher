package com.alecktos.stockfetcher;


import com.alecktos.DateTime;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.alecktos.logger.Logger;
import com.alecktos.stocklibrary.configs.DisneyAvanzaConfig;

public class Main {

	public static void main(String[] args) {
		String mode = args.length > 0 ? args[0] : "emulation";
		Logger.doAlert("Stockfetcher: " + mode);

		Module module = new StockfetcherDependencyModule() {
		};

		Injector injector = Guice.createInjector(module);

		final StockfetcherRunner stockfetcherRunner = injector.getInstance(StockfetcherRunner.class);
		stockfetcherRunner.run(DateTime.createFromNow(), new DisneyAvanzaConfig());
	}

}