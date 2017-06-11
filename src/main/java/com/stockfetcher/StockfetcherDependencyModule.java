package com.stockfetcher;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.logger.EmailNotifier;
import com.logger.Logger;

abstract public class StockfetcherDependencyModule extends AbstractModule {

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
	}

}
