package com.alecktos.stockfetcher.influxdb;

import com.alecktos.misc.DateTime;
import com.alecktos.misc.FileHandler;
import com.alecktos.misc.LineFileReader;
import com.alecktos.misc.logger.Logger;
import com.alecktos.stockfetcher.StockFileLineExtractor;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class InfluxDbTest {

	@Test
	public void testCreatingStock() {
		LineFileReader lineFileReader= new LineFileReader();
		final List<String> lines = lineFileReader.getLinesFromFile("/home/alexander/Downloads/disney.txt");

		lines.remove(0); //remove meta line
		lines.forEach(line -> {
			final StockFileLineExtractor stockFileLineExtractor = new StockFileLineExtractor(line);

			final String insertQuery = String.format("disney_stock stock_value=%s %s",
					stockFileLineExtractor.getPriceFromRow(),
					DateTime.createFromDateTimeString(stockFileLineExtractor.getDateTimeStringFromRow()).toTimeStamp()
			);

			try {
				executePost("http://localhost:8086/write?db=stocks&precision=ms&", insertQuery);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	@Test
	public void testFetchingLinesLocally() {
		try {
			String query = "select * from disney_stock";
			String q = URLEncoder.encode(query, "UTF-8");
			executeGet("http://localhost:8086/query?db=stocks&pretty=true&q=" + q);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void executeGet(String endpoint) throws IOException {
		URL url = new URL(endpoint);
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null)
			System.out.println(inputLine);
		in.close();
	}

	public static void executePost(String endpoint, String data)
			throws ClientProtocolException, IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(endpoint);

		StringEntity entity = new StringEntity(data);
		httpPost.setEntity(entity);

		CloseableHttpResponse response = client.execute(httpPost);

		System.out.println("status line: " + response.getStatusLine());

		client.close();
	}


}
