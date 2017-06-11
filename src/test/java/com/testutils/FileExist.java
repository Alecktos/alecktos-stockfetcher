package com.testutils;

import java.io.File;

public class FileExist {

	public static boolean doExist(String filePath) {
		File f = new File(filePath);
		return f.exists() && !f.isDirectory();
	}

}
