package com.alecktos.stockfetcher.markitondemand;


import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MarkItOnDemandTest {

	@Test
	public void testFetchingPrice() {
		MarkItOnDemand markItOnDemand = new MarkItOnDemand();
		double currentStockPrice = markItOnDemand.getCurrentStockPrice();
		assertTrue(currentStockPrice > 0);
	}

}
