package com.stocklibrary;

import com.DateTime;

import javax.inject.Singleton;
import java.io.Serializable;

@Singleton
public class OpenStockHandler implements Serializable {

	public boolean isOpen(DateTime dateTime, String openingTime, String closingTime) {
		if(!dateTime.isTimeInInterval(openingTime, closingTime)) {
			return false;
		}

		return BusinessDayUtil.isBusinessDay(dateTime.getCalendarFromDateTime().getTime());
	}
}
