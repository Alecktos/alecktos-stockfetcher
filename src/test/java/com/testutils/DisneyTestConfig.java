package com.testutils;

import com.alecktos.stocklibrary.configs.DisneyAvanzaConfig;

public class DisneyTestConfig extends DisneyAvanzaConfig {

	public void setPath(String path) {
		this.archivePath = path;
	}

	public void setCandleStickInterval(int candleStickInterval) {
		this.candleStickInterval = candleStickInterval;
	}

	public void setAverageDays(int averageDays) {
		this.averageDays = averageDays;
	}

	public void setbuyAmountOffSaldo(double buySellAmountOffSaldo) {
		this.buySellAmountOffSaldo = buySellAmountOffSaldo;
	}

	public void setStartSaldo(final double startSaldo) {
		this.startSaldo = startSaldo;
	}

	public void setBuyAmountOffSaldo(final double buyAmountOffSaldo) {
		this.buySellAmountOffSaldo = buyAmountOffSaldo;
	}

	public void setCourtage(final double courtage) {
		this.courtage = courtage;
	}

	public void setCourtageMin(final double courtageMin) {
		this.courtageMin = courtageMin;
	}
}
