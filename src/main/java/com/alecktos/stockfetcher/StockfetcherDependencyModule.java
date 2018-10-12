package com.alecktos.stockfetcher;

import com.alecktos.misc.logger.EmailNotifier;
import com.alecktos.misc.logger.Logger;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Names;

abstract public class StockfetcherDependencyModule extends AbstractModule {

	private String emailConfigPath;

	public StockfetcherDependencyModule(String emailConfigPath) {
		this.emailConfigPath = emailConfigPath;
	}

	protected Provider<Logger> getLoggerProvider() {
		return new Provider<Logger>() {

			@Inject
			EmailNotifier emailNotifier;

			@Override
			public Logger get() {
				return new Logger(emailNotifier);
			}
		};
	}

	@Override
	protected void configure() {
		bind(Logger.class).toProvider(getLoggerProvider());
		bindConstant().annotatedWith(Names.named("emailConfigPath")).to(emailConfigPath);
		bindConstant().annotatedWith(Names.named("influxDbName")).to("stocks");
	}

}
