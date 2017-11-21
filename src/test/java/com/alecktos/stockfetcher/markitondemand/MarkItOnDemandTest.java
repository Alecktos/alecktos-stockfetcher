package com.alecktos.stockfetcher.markitondemand;

import com.alecktos.stockfetcher.candles.CandleStick;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MarkItOnDemandTest {

	@Test
	public void testParseChart() {
		MarkItOnDemand markItOnDemand = new MarkItOnDemand();

		CandleStick[] candleSticks = markItOnDemand.parseChart("2015-10-26", "2015-10-29");
		assertEquals(3, candleSticks.length);

		candleSticks = markItOnDemand.parseChart("2015-10-19", "2015-10-23");
		assertEquals(4, candleSticks.length);


	}

}
