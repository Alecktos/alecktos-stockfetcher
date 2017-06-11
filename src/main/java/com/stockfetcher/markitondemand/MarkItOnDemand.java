package com.stockfetcher.markitondemand;

import com.candles.CandleStick;

/**
 * http://dev.markitondemand.com/MODApis/#stockquote
 * <p>
 * Created by Alexander on 2015-11-21.
 */
public class MarkItOnDemand {

	CandleStick[] parseChart(String startDate, String endDate) {
		MarkItOnDemandJSON markItDownJSON = new MarkItOnDemandJSON();
		return markItDownJSON.getHistoricStockData(startDate, endDate);
	}

	public double getCurrentStockPrice() {
		MarkItOnDemandJSON markItOnDemandJSON = new MarkItOnDemandJSON();
		return markItOnDemandJSON.getCurrentPrice();
	}
}


