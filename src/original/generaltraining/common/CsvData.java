package original.generaltraining.common;

import java.util.List;

public class CsvData {

	private String[] csvItem;
	
	public void itemSet(List<String> csvData) {
		setCsvItem(csvData.get(0).split(","));
	}
	
	public String[] getCsvItem() {
		return csvItem;
	}
	public void setCsvItem(String[] csvItem) {
		this.csvItem = csvItem;
	}
	
	
}
