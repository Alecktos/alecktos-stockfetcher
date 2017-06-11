package com.alecktos.candles;


public class CandleStick {

	private String openDateTime;
	private Number openPrice;

	private Number closePrice;
	private String closeDateTime;

	public CandleStick() {
	}

	public CandleStick(Number openPrice, Number closePrice) {
		setClosePrice(closePrice);
		setOpenPrice(openPrice);
	}

	void setClosePrice(Number value, String dateTime) {
		closeDateTime = dateTime;
		setClosePrice(value);
	}

	public void setClosePrice(Number value) {
		closePrice = value;
	}

	void setOpenPrice(Number value, String dateTime) {
		openDateTime = dateTime;
		setOpenPrice(value);
	}

	public void setOpenPrice(Number value) {
		openPrice = value;
	}

	public Number getClosePrice() {
		return closePrice;
	}

	public String getCloseDateTimeString() {
		return closeDateTime;
	}

	public Number getOpenPrice() {
		return openPrice;
	}

	public String getOpenDateTimeString() {
		return openDateTime;
	}
}
