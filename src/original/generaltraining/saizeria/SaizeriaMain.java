package original.generaltraining.saizeria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import original.generaltraining.common.CsvMethod;

public class SaizeriaMain {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		List<String> csvData = CsvMethod.csvChangeList("saizeria.csv");
		List<SaizeriaMenu> menu = SaizeriaMenu.changeData(csvData);

		int priceLimit = 1000;
		String keyword = "無し";
		String genru = "無し";
		String genruNum = null;
		List<String> genruList = Arrays.asList("【12】サラダ", "【13】スープ", "【14】おつまみ", "【21】ドリア", "【22】ピザ",
				"【23】パスタ", "【24】肉料理", "【31】パン/ライス", "【32】デザート", "【33】その他アルコール",
				"【34】ワイン", "【51】ドリンクバー");

		boolean changeSetting = false;
		boolean setKeyword = false;
		boolean setGenru = false;

		while (true) {
			selectMenu();
			int selectMenu = sc.nextInt();
			switch (selectMenu) {
			case 1:
				listView(csvData);
				System.out.println();
				break;
			case 2:
				if (changeSetting && setKeyword && !setGenru) {
					List<SaizeriaMenu> result = gacha(menu, priceLimit, keyword);
					settingView(changeSetting, priceLimit, keyword, genru);
					resultGacha(result);
				} else if (changeSetting && setGenru) {
					List<SaizeriaMenu> result = gacha(menu, priceLimit, keyword, genruNum);
					settingView(changeSetting, priceLimit, keyword, genru);
					resultGacha(result);
				} else {
					List<SaizeriaMenu> result = gacha(menu, priceLimit);
					settingView(changeSetting, priceLimit, keyword, genru);
					resultGacha(result);
				}

				break;
			case 3:
				settingMenu();
				int selectSetting = sc.nextInt();
				switch (selectSetting) {
				case 1:
					System.out.print("設定したい金額を入力してください：");
					priceLimit = sc.nextInt();
					System.out.println();
					changeSetting = true;
					break;
				case 2:
					System.out.print("必須にしたいキーワードを入力してください：");
					String inputKeyword = sc.next();
					boolean hit = false;
					for (int i = 0; i < menu.size(); i++) {
						SaizeriaMenu check = menu.get(i);
						if (check.getItemName().contains(inputKeyword)
								&& (genruNum == null || check.getOrderCode().substring(0, 2).equals(genruNum))) {
							hit = true;
							break;
						}
					}
					if (hit) {
						keyword = inputKeyword;
						changeSetting = true;
						setKeyword = true;
					} else {
						System.out.println("****キーワードを含む商品はありません****");
						System.out.println("****設定しなおしてください          ****");
					}
					System.out.println();
					break;
				case 3:
					System.out.println();
					for (int i = 0; i < genruList.size(); i++) {
						System.out.print(genruList.get(i));
						if (i != 0 && i % 3 == 0) {
							System.out.println();
						}
					}
					System.out.println();
					System.out.print("上記のジャンルの中から、必須にしたいジャンルを入力してください→");
					String inputGenru = sc.next();
					for (String check : genruList) {
						if (check.contains(inputGenru)) {
							genru = check.substring(4);
							genruNum = check.substring(1, 3);
							changeSetting = true;
							setGenru = true;
							break;
						}
					}

					boolean keyCheck = true;
					if (setKeyword) {
						for (SaizeriaMenu sm : menu) {
							if (sm.getItemName().contains(keyword)
									&& sm.getOrderCode().substring(0, 2).equals(genruNum)) {
								keyCheck = true;
								break;
							}
							keyCheck = false;
						}
					}

					if (!setGenru) {
						System.out.println("\n***ジャンル選択エラー***");
					} else if (!keyCheck) {
						System.out.println("\n***設定に当てはまる商品がありません***");
						genru = "無し";
						genruNum = null;
						setGenru = false;
					}
					System.out.println();

					break;
				default:
					break;
				}

				break;
			case 4:
				priceLimit = 1000;
				keyword = "無し";
				genru = "無し";
				genruNum = null;
				changeSetting = false;
				setKeyword = false;
				setGenru = false;
				System.out.println("設定をリセットしました\n");
				break;
			default:
				break;
			}

			if (selectMenu == 5) {
				System.out.println("\n****終了****");
				break;
			} else if (selectMenu == 3 || selectMenu == 4) {
				continue;
			}
		}

		System.out.println();

		sc.close();
	}

	public static void selectMenu() {
		System.out.println("操作したい内容を選んでください");
		System.out.print("1.商品一覧を表示 2.ガチャを回す 3.設定 4.設定のリセット 5.終了：");
	}

	public static void settingMenu() {
		System.out.print("1.金額の変更 2.必須商品の設定 3.必須ジャンルの設定 4.戻る ：");
	}

	public static int settingPriceLimit(int priceLimit) {
		return priceLimit;
	}

	public static void listView(List<String> menu) {
		System.out.println();
		String[][] strArr = new String[menu.size()][];
		for (int i = 0; i < strArr.length; i++) {
			strArr[i] = menu.get(i).split(",");
		}

		int maxCordCnt = 0;
		int maxNameCnt = 0;
		int maxPriceCnt = 0;

		for (int i = 0; i < strArr.length; i++) {
			maxCordCnt = Math.max(maxCordCnt, getHan1Zen2(strArr[i][0]));
			maxNameCnt = Math.max(maxNameCnt, getHan1Zen2(strArr[i][1]));
			maxPriceCnt = Math.max(maxPriceCnt, getHan1Zen2(strArr[i][2]));
		}

		for (int i = 0; i < strArr.length; i++) {
			for (int j = 0; j < strArr[i].length; j++) {
				int cnt = 0;
				if (j == 0) {
					System.out.print(strArr[i][j]);
					cnt = maxCordCnt - getHan1Zen2(strArr[i][0]);
					for (int k = 0; k < cnt + 5; k++) {
						System.out.print(" ");
					}
				} else if (j == 1) {
					System.out.print(strArr[i][j]);
					cnt = maxNameCnt - getHan1Zen2(strArr[i][1]);
					for (int k = 0; k < cnt + 5; k++) {
						System.out.print(" ");
					}
				} else {
					if (i > 0) {
						System.out.printf("%,5d円", Integer.parseInt(strArr[i][j]));
					} else {
						System.out.printf("  %s", strArr[i][j]);
					}
				}

			}
			System.out.println();
		}
	}

	public static List<SaizeriaMenu> gacha(List<SaizeriaMenu> menu, int priceLimit) {
		int priceMin = priceLimit;

		for (SaizeriaMenu sm : menu) {
			priceMin = Math.min(priceMin, sm.getPrice());
		}

		//ガチャ
		int priceTotal = 0;
		var result = new ArrayList<SaizeriaMenu>();
		int cnt = 0;
		while (true) {
			int gacha = new Random().nextInt(menu.size());
			SaizeriaMenu select = menu.get(gacha);
			cnt++;
			if (priceTotal + select.getPrice() > priceLimit) {
				if (priceLimit - priceTotal < priceMin) {
					break;
				} else if (cnt == menu.size()) {
					break;
				}
				continue;
			}
			priceTotal += select.getPrice();
			result.add(select);
		}
		return result;
	}

	public static int getHan1Zen2(String str) {

		//戻り値
		int ret = 0;

		//全角半角判定
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (String.valueOf(c[i]).getBytes().length <= 1) {
				ret += 1; //半角文字なら＋１
			} else {
				ret += 2; //全角文字なら＋２
			}
		}

		return ret;
	}

	public static List<SaizeriaMenu> gacha(List<SaizeriaMenu> menu, int priceLimit, String keyword) {
		int priceMin = priceLimit;

		for (SaizeriaMenu sm : menu) {
			priceMin = Math.min(priceMin, sm.getPrice());
		}

		int priceTotal = 0;
		var result = new ArrayList<SaizeriaMenu>();
		boolean must = false;
		while (true) {
			int gacha = new Random().nextInt(menu.size());
			SaizeriaMenu select = menu.get(gacha);
			if (select.getItemName().contains(keyword) && !must) {
				must = true;
			} else if (!must) {
				continue;
			}

			if (priceTotal + select.getPrice() > priceLimit) {

				if (priceLimit - priceTotal < priceMin) {
					break;
				}
				continue;
			}
			priceTotal += select.getPrice();
			result.add(select);
		}
		return result;
	}

	public static List<SaizeriaMenu> gacha(List<SaizeriaMenu> menu, int priceLimit, String keyword, String genruNum) {
		int priceMin = priceLimit;

		for (SaizeriaMenu sm : menu) {
			priceMin = Math.min(priceMin, sm.getPrice());
		}

		int priceTotal = 0;
		var result = new ArrayList<SaizeriaMenu>();
		boolean mustGenru = false;
		while (true) {
			int gacha = new Random().nextInt(menu.size());
			SaizeriaMenu select = menu.get(gacha);

			if (keyword.equals("無し") && select.getOrderCode().substring(0, 2).equals(genruNum)) {
				mustGenru = true;
			} else if (select.getItemName().contains(keyword)
					&& select.getOrderCode().substring(0, 2).equals(genruNum)) {
				mustGenru = true;
			}

			if (!mustGenru) {
				continue;
			}

			if (priceTotal + select.getPrice() > priceLimit) {

				if (priceLimit - priceTotal < priceMin) {
					break;
				}
				continue;
			}
			priceTotal += select.getPrice();
			result.add(select);
		}
		return result;
	}

	public static void settingView(boolean changeSetting, int priceLimit, String keyword, String genru) {
		if (changeSetting) {
			System.out.println("\n【設定内容】");
			System.out.println("設定金額：" + priceLimit + "円");
			System.out.println("必須商品キーワード：" + keyword);
			System.out.println("必須ジャンル：" + genru);
		}
	}

	public static void resultGacha(List<SaizeriaMenu> result) {
		int totalPrice = 0;
		int num = 1;
		System.out.println("\n***********ガチャ結果***********");
		for (SaizeriaMenu sm : result) {
			totalPrice += sm.getPrice();
			System.out.print("（" + num + "）");
			System.out.println(sm.getOrderCode() + "：" + sm.getItemName() + "（" + sm.getPrice() + "円）");
			num++;
		}
		System.out.println("合計金額：" + totalPrice + "円");
		System.out.println("********************************\n");
	}

}
