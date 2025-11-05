package original.generaltraining.saizeria;

import java.util.ArrayList;
import java.util.List;

import original.generaltraining.common.CsvData;

public class SaizeriaMenu extends CsvData {
	private String itemName;
	private String orderCode;
	private int price;
	public static List<SaizeriaMenu> changeData(List<String> menuListS) {
		List<SaizeriaMenu> menuList = new ArrayList<SaizeriaMenu>();
		for (String raws : menuListS) {
			String[] column = raws.split(",");
			if (column[2].equals("値段")) {
				continue;
			}
			SaizeriaMenu sm = new SaizeriaMenu();
			sm.setOrderCode(column[0]);
			sm.setItemName(column[1]);
			sm.setPrice(Integer.parseInt(column[2]));
			menuList.add(sm);
		}
		
		return menuList;
	}
	
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
}