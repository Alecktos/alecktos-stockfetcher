package com.alecktos.stocklibrary.configs;

abstract public class Config {

	protected String archivePath;
	protected int candleStickInterval;
	protected int averageDays;
	protected double buySellAmountOffSaldo;
	protected double courtage;
	protected double courtageMin;
	protected double startSaldo; //in stock currency
	protected String openingTime;
	protected String closingTime;

	public double getBuyAmountOffSaldo() {
		if(this.buySellAmountOffSaldo <= 0) {
			throw new RuntimeException("buySellAmountOffSaldo not set correctly");
		}
		return buySellAmountOffSaldo;
	}

	public int getAverageDays() {
		if(this.averageDays <= 0) {
			throw new RuntimeException("averageDays not set correctly");
		}
		return averageDays;
	}

	public int getCandleStickInterval() {
		if(this.candleStickInterval < 5) {
			throw new RuntimeException("candlestickTime not set correctly");
		}
		return candleStickInterval;
	}

	public String getArchivePath() {
		if(this.archivePath == null || this.archivePath.length() == 0) {
			throw new RuntimeException("archivePath not set correctly");
		}
		return archivePath;
	}

	public double getCourtage() {
		if(courtage <= 0) {
			throw new RuntimeException("courtage is not set correctly");
		}
		return courtage;
	}

	public double getCourtageMin() {
		if(courtageMin <= 0) {
			throw new RuntimeException("courtageMin is not se correctly");
		}
		return courtageMin;
	}

	public double getStartSaldo() {
		if(startSaldo <= 0) {
			throw new RuntimeException("startSaldo is not set correctly");
		}
		return startSaldo;
	}

	public String getOpeningTime() {
		if(openingTime == null || openingTime.length() == 0) {
			throw new RuntimeException("openingTime is not set correctly");
		}
		return openingTime;
	}

	public String getClosingTime() {
		if(closingTime == null || closingTime.length() == 0) {
			throw new RuntimeException("closingTime is not set correctly");
		}
		return closingTime;
	}

}
