package com.alecktos.logger;

import com.alecktos.DateTime;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class Logger implements Serializable {

	//TODO nothing should be static
	private static List<Class> ignoreClasses = new ArrayList<>();
	private static String subject = null;
	private static boolean alert = false;

	private AlertNotifierInterface alertNotifier;

	@Inject
	public Logger(final AlertNotifierInterface alertNotifier) {
		this.alertNotifier = alertNotifier;
	}

	//TODO: need to remove this
	public static void doAlert(String subject) {
		alert = true;
		Logger.subject = subject;
	}

	public void setIgnore(Class[] excludedClasses) {
		ignoreClasses = new ArrayList<>(Arrays.asList(excludedClasses));
	}

	public void log(String info, Class callingClass) {
		if (ignoreClasses.contains(callingClass)) {
			return;
		}

		String formattedInfo = getFormattedInfo(info, callingClass.getSimpleName());
		System.out.println(formattedInfo);
	}

	public void logAndAlert(String info, Class callingClass) {
		log(info, callingClass);
		if(alert) {
			alertNotifier.notify(getFormattedInfo(info, callingClass.getSimpleName()), subject);
		}
	}

	private String getFormattedInfo(String info, String simpleName) {
		String utcTime = DateTime.createFromNow().toString();
		return "Current UTC Time: " + utcTime + ": " + info + " [Class: " + simpleName + "]";
	}
}