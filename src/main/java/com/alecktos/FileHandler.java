package com.alecktos;

import java.io.*;
import java.nio.file.FileSystems;

import static java.nio.file.Files.copy;
import static java.nio.file.Files.deleteIfExists;

public class FileHandler {

	public static PrintWriter getFileWriter(String filePath) throws IOException {
		return new PrintWriter(new FileWriter(filePath, true));
	}

	public static BufferedReader getFileReader(String filePath) throws FileNotFoundException {
		return new BufferedReader(new FileReader(filePath));
	}

	public static void writeObjectToDisk(Serializable object, String fileName) throws IOException {
		FileOutputStream f_out = new FileOutputStream(fileName);
		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
		obj_out.writeObject (object);
		obj_out.close();
		f_out.close();
	}

	public static Object readObjectFromDisk(String filePath) throws IOException, ClassNotFoundException {
		FileInputStream fin = new FileInputStream(filePath);
		ObjectInputStream ois = new ObjectInputStream(fin);
		Object result = ois.readObject();
		ois.close();
		return result;
	}

	public static void deleteFile(String path) throws IOException {
		deleteIfExists(FileSystems.getDefault().getPath(path));
	}

	public static void copyFile(String sourcePath, String destinationPath) throws IOException {
		copy(new File(sourcePath).toPath(), new File(destinationPath).toPath());
	}

}
