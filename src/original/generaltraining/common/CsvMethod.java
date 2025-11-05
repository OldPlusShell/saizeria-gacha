package original.generaltraining.common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvMethod {
	public static List<String> csvChangeList(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		List<String> csvList = new ArrayList<String>();
		
		String rec;
		while ((rec = br.readLine()) != null) {
			csvList.add(rec);
		}
		
		br.close();
		
		return csvList;
	}
	
	public static void csvWriter(String fileName, List<String> csvData) throws IOException {
		FileWriter fw = new FileWriter(fileName);
		for (String raws : csvData) {
			fw.write(raws + "\r\n");
		}
		fw.close();
		
	}
}
