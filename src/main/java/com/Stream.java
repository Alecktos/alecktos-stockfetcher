package com;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Stream {

	public InputStream getInputStream(String urlPath) throws IOException {
		URL url = new URL(urlPath);
		URLConnection connection = url.openConnection();
		return connection.getInputStream();
	}
}