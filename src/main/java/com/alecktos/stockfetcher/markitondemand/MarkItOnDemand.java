package com.alecktos.stockfetcher.markitondemand;

/**
 * http://dev.markitondemand.com/MODApis/#stockquote
 *
 */
public class MarkItOnDemand {

	public double getCurrentStockPrice() {
		MarkItOnDemandJSON markItOnDemandJSON = new MarkItOnDemandJSON();
		return markItOnDemandJSON.getCurrentPrice();
	}
}


