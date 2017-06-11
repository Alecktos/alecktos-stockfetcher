package com.stockfetcher.markitondemand;

import com.Stream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;


class MarkItOnDemandXML {

	void parseStockInfo() {
		Document document = getXMLDocument("http://dev.markitondemand.com/MODApis/Api/v2/Quote?Symbol=TSLA");
		String lastPrice = getNodeValueFromDocument(document, "LastPrice");
		System.out.println(lastPrice);
	}

	private Document getXMLDocument(String urlPath) {
		try {
			InputStream stream = new Stream().getInputStream(urlPath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			return db.parse(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getNodeValueFromDocument(Document document, String tagName) {
		NodeList nl = document.getElementsByTagName(tagName);
		Element el = (Element) nl.item(0);
		Text elText = (Text) el.getFirstChild();
		return elText.getNodeValue();
	}
}
