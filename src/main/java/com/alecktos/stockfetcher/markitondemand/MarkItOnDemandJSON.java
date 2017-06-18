package com.alecktos.stockfetcher.markitondemand;

import com.alecktos.misc.Stream;
import com.alecktos.candles.CandleStick;
import com.alecktos.misc.logger.Logger;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.*;
import java.net.URLEncoder;


class MarkItOnDemandJSON {

	@Inject
	private Logger logger;

	/**
	 * @param startDate
	 * @param endDate   Last candlestick will be the day before endDate
	 * @return candlesticks. The day before endDate will be last in array.
	 */
	CandleStick[] getHistoricStockData(String startDate, String endDate) {
		String content = "";

		try {
			String urlPath = getHistoricDataUrl(startDate, endDate);
			content = getJSONContent(urlPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

		MarkItOnDemandJSONHistoricStockParser parser = new MarkItOnDemandJSONHistoricStockParser(content);
		return parser.getHistoricCandleSticks();
	}

	double getCurrentPrice() {
		String content = getJSONContent("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?Symbol=DIS");
		JSONObject jsonContentObject = new JSONObject(content);
		return jsonContentObject.getDouble("LastPrice");
	}

	/**
	 * @param startDate
	 * @param endDate   Last candlestick will be the day before endDate
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getHistoricDataUrl(String startDate, String endDate) throws UnsupportedEncodingException {
		//har testat utförligt och inga intra dagsrequest fungerar

		final String paramsObject = "{" +
				"\"Normalized\":false," + //show percent if true. In price if false
//                    "\"NumberOfDays\":100," +
				"\"StartDate\":\"" + startDate + "T00:00:00-00\"," +
				"\"EndDate\":\"" + endDate + "T00:00:00-00\"," +
				"\"EndOffsetDays\":0," +


				"\"DataPeriod\":\"DAY\"," +
				//"\"DataInterval\":1," +

				//"\"LabelPeriod\":\"HOUR\"," +
				//"\"LabelInterval\":\"1\"," +

				//"\"NumberOfDays\":10," +
				"\"Elements\":[" +
				"{\"Symbol\":\"DIS\",\"Type\":\"price\",\"Params\":[\"ohlc\"]}" +
				"]" +
				"}";

		final String urlBasePath = "http://dev.markitondemand.com/Api/v2/InteractiveChart/json?parameters=";

		return urlBasePath + URLEncoder.encode(paramsObject, "UTF-8");
	}

	private String getJSONContent(final String urlPath) {
		String fileContent = "";
		try {
			InputStream stream = new Stream().getInputStream(urlPath);
			BufferedReader bReader = new BufferedReader(new InputStreamReader(stream));
			String line;
			while ((line = bReader.readLine()) != null) {
				fileContent += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.logAndAlert("IOException: " + e.getMessage(), getClass());
		}
		return fileContent;
	}
}
