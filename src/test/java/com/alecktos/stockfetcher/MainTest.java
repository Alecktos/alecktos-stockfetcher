package com.alecktos.stockfetcher;

import com.alecktos.FileHandler;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;

public class MainTest {

	@Test
	public void testStockFetcherWhenNoFile() {
		Main.main(new String[]{"production", "disney.txt", "14:30:00", "21:00:00"});
	}

	@Test
	public void testStockFetcherWithEmptyFiles() {
		try(PrintWriter printWriter = FileHandler.getFileWriter("disney.txt")) {
			printWriter.println("content-interval:5m  open:14.30-21.00 #time is in UTC");
		}catch (IOException e) {
			throw new RuntimeException("Failed saving price to file: " + e.getMessage() + e.getStackTrace().toString());
		}

		Main.main(new String[]{"production", "disney.txt", "14:30:00", "21:00:00"});

		try {
			FileHandler.deleteFile("disney.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testStockFetcherOnExistingFileMain() {
		try (PrintWriter printWriter = FileHandler.getFileWriter("disney.txt")) { //By putting it in a try statement I ensure that it is always closed (java 7 and above feature).
			//printWriter.println();
			printWriter.println("content-interval:5m  open:14.30-21.00 #time is in UTC");
			printWriter.println("95.32:1454682605230");
			printWriter.println("95.11:1454682905227");
			printWriter.println("95.16:1454683204840");
			printWriter.println("95.04:1454683505497");
			printWriter.println("94.71:1454683805605");
			printWriter.println("94.74:1454684105237");
			printWriter.println("94.75:1454684404867");
			printWriter.println("94.86:1454684705488");
			printWriter.println("94.6:1454685005443");
			printWriter.println("94.85:1454685305084");
			printWriter.println("94.83:1454685604953");
			printWriter.println("94.56:1454685904577");
			printWriter.println("94.38:1454686205253");
			printWriter.println("94.22:1454686504903");
			printWriter.println("94.04:1454686805502");
			printWriter.println("94.4:1454687105923");
			printWriter.println("94.42:1454687405628");
			printWriter.println("94.11:1454687705277");
			printWriter.println("93.95:1454688005168");
			printWriter.println("93.86:1454688304818");
			printWriter.println("93.87:1454688605478");
			printWriter.println("93.91:1454688905182");
			printWriter.println("93.73:1454689205069");
			printWriter.println("93.57:1454689504728");
			printWriter.println("93.86:1454689805369");
			printWriter.println("93.82:1454690105205");
			printWriter.println("93.86:1454690405019");
			printWriter.println("93.8:1454690704862");
			printWriter.println("93.9:1454691005733");
			printWriter.println("93.96:1454691305540");
			printWriter.println("93.92:1454691605344");
			printWriter.println("94.05:1454691905195");
			printWriter.println("94.05:1454692204889");
			printWriter.println("94.02:1454692505577");
			printWriter.println("93.99:1454692805449");
			printWriter.println("94.11:1454693105171");
			printWriter.println("94.0:1454693405070");
			printWriter.println("93.99:1454693705269");
			printWriter.println("94.23:1454694005109");
			printWriter.println("94.26:1454694304901");
			printWriter.println("94.29:1454694605579");
			printWriter.println("94.26:1454694905214");
			printWriter.println("94.31:1454695204908");
			printWriter.println("94.37:1454695510852");
			printWriter.println("94.49:1454695805603");
			printWriter.println("94.3:1454696120330");
			printWriter.println("94.35:1454696405253");
			printWriter.println("94.49:1454696705024");
			printWriter.println("94.54:1454697004763");
			printWriter.println("94.57:1454697305432");
			printWriter.println("94.37:1454697605554");
			printWriter.println("94.38:1454697905278");
			printWriter.println("94.23:1454698204929");
			printWriter.println("94.28:1454698504642");
			printWriter.println("94.22:1454698805295");
			printWriter.println("94.28:1454699104993");
			printWriter.println("94.2:1454699404702");
			printWriter.println("94.08:1454699705383");
			printWriter.println("94.06:1454700005257");
			printWriter.println("94.13:1454700305171");
			printWriter.println("93.96:1454700604879");
			printWriter.println("94.13:1454700905673");
			printWriter.println("94.15:1454701205607");
			printWriter.println("93.89:1454701505291");
			printWriter.println("93.91:1454701804877");
			printWriter.println("93.79:1454702105780");
			printWriter.println("93.8:1454702405410");
			printWriter.println("93.52:1454702705093");
			printWriter.println("93.68:1454703004779");
			printWriter.println("93.65:1454703305437");
			printWriter.println("93.87:1454703605324");
			printWriter.println("93.87:1454703905015");
			printWriter.println("93.85:1454704204667");
			printWriter.println("93.8:1454704505332");
			printWriter.println("93.57:1454704805000");
			printWriter.println("93.8:1454705104670");
			printWriter.println("93.66:1454705405379");
			printWriter.println("93.6:1454705705071");

		} catch (IOException e) {
			throw new RuntimeException("Failed saving price to file: " + e.getMessage() + e.getStackTrace().toString());
		}

		Main.main(new String[]{"production", "disney.txt", "14:30:00", "21:00:00"});

		try {
			FileHandler.deleteFile("disney.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
