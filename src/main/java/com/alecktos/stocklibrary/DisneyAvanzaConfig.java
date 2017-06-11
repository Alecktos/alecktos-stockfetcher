package com.alecktos.stocklibrary.configs;

import javax.inject.Inject;

public class DisneyAvanzaConfig extends Config {

	@Inject
	public DisneyAvanzaConfig() {
		this.archivePath = "disney.txt";
		this.candleStickInterval = 130;
		this.averageDays = 16;
		this.buySellAmountOffSaldo = 1;
		this.startSaldo = 1000;
		this.courtage = 0.0025;
		this.courtageMin = 1;
		this.openingTime = "14:30:00";
		this.closingTime = "21:00:00";
	}

}