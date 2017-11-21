package com.alecktos.stockfetcher.markitondemand;

import com.alecktos.stockfetcher.candles.CandleStick;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;


class MarkItOnDemandJSONHistoricStockParser {

	private CandleStick[] candleSticks;
	private JSONObject jsonContentObject;

	MarkItOnDemandJSONHistoricStockParser(String content) {
		jsonContentObject = new JSONObject(content);
		generateCandleSticksArray();
		addValues();
	}

	CandleStick[] getHistoricCandleSticks() {
		return candleSticks;
	}

	private void generateCandleSticksArray() {
		int numberOffCandleSticks = jsonContentObject.getJSONArray("Positions").length();
		candleSticks = new CandleStick[numberOffCandleSticks];
		Arrays.asList(candleSticks).replaceAll(candleStick -> new CandleStick()); //fill array with candlestick objects

	}

	private void addValues() {
		JSONArray closeValues = getValuesFromDataSeries("close");
		JSONArray openValues = getValuesFromDataSeries("open");
		for (int i = 0; i < candleSticks.length; i++) {
			candleSticks[i].setClosePrice((Number) closeValues.get(i));
			candleSticks[i].setOpenPrice((Number) openValues.get(i));
		}
	}

	private JSONArray getValuesFromDataSeries(String type) {
		return jsonContentObject.getJSONArray("Elements")
				.getJSONObject(0) //elements is an array that only contains one element
				.getJSONObject("DataSeries")
				.getJSONObject(type)
				.getJSONArray("values");
	}

}
