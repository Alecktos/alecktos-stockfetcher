package com.alecktos.logger;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.io.Serializable;

public class EmailNotifier implements AlertNotifierInterface, Serializable {

	public void notify(String message, String subject) {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.mail.yahoo.com");
			email.setSmtpPort(465);
			email.setAuthenticator(new DefaultAuthenticator("alerts.ale@yahoo.com", "Hbw3Yam*pOOc119o"));
			email.setSSLOnConnect(true);
			email.setFrom("alerts.ale@yahoo.com");
			email.setSubject(subject);

			email.setTextMsg(message);
			email.addTo("persson33@gmail.com");
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}

	}

}
