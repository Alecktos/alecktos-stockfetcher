package com.testutils;

import com.alecktos.misc.FileHandler;

import java.io.IOException;
import java.io.PrintWriter;

public class EmailTestConfig {

	private static final String emailTestConfigPath = "email_config.yml";

	public static void create() {

		try(PrintWriter printWriter = FileHandler.getFileWriter(emailTestConfigPath)) {
			printWriter.println("email:");
			printWriter.println("  authentication:");
			printWriter.println("    username: stuff.ae@yahoo.com");
			printWriter.println("    password: fhdjsk12!");
			printWriter.println("  hostname: smtp.mail.gmail.com");
			printWriter.println("  fromaddress: a.dse@yahoo.com");
			printWriter.println("  receiveraddress: fhdsjk@gmail.com");
		}catch (IOException e) {
			throw new RuntimeException("Failed saving file: " + e.getMessage() + e.getStackTrace().toString());
		}
	}

	public static void destroy() {
		try {
			FileHandler.deleteFile(emailTestConfigPath);
		} catch (IOException e) {
			throw new RuntimeException("Failed saving file: " + e.getMessage() + e.getStackTrace().toString());
		}
	}

}
