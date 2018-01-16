package com.alecktos.stockfetcher.markitondemand;

import com.alecktos.misc.Stream;
import com.alecktos.misc.logger.Logger;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


class MarkItOnDemandJSON {

	@Inject
	private Logger logger;

	double getCurrentPrice() {
		String content = getJSONContent("http://dev.markitondemand.com/MODApis/Api/v2/Quote/json?Symbol=DIS");
		JSONObject jsonContentObject = new JSONObject(content);
		return jsonContentObject.getDouble("LastPrice");
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
