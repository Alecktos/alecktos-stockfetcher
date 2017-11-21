package com.testutils;

import com.alecktos.misc.FileHandler;

import java.io.IOException;

import static org.junit.Assert.fail;

public class TestStockFile {

	private static String testStockFilePath = "src/test/disneyStockTest.txt";

	public static String getFilePath() {
		return testStockFilePath;
	}

	public static void removeFile() {
		try {
			FileHandler.deleteFile(testStockFilePath);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	public static boolean fileExist() {
		return FileExist.doExist(testStockFilePath);
	}


}
