package com.testutils;

import com.DateTime;
import com.stocklibrary.configs.Config;

public class MockConfig extends Config {

	public static MockConfig createMockConfigOpenMarketFromDateTime(final String filePath, final DateTime dateTime) {
		return new MockConfig(
				filePath,
				dateTime.createDateTimeWithTimeRewind(5).toTimeString(),
				dateTime.createDateTimeWithTimeForward(5).toTimeString()
		);
	}

	private MockConfig(String archivePath, String openingTime, String closingTime) {
		this.archivePath = archivePath;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
	}

}
