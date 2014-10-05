import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class RaceResult {
	public static final int RACE_KIND_HORSE = 0, RACE_KIND_CYCLE = 1,
			RACE_KIND_BOAT = 2;
	public static final int RACE_GRD_HORSE_SEOUL = 0,
			RACE_GRD_HORSE_JEJU = 1, RACE_GRD_HORSE_PKNU = 2;
	public static final int RACE_GRD_CYCLE_GWANGMYEONG = 0,
			RACE_GRD_CYCLE_CHANGWON = 1, RACE_GRD_CYCLE_PUSAN = 2;
	public static final int RACE_GRD_BOAT_MISARI = 0;

	int race_uid;
	int race_kind;
	Date race_date;
	int race_grd;
	int race_nPlayer;
	int race_no;
	int race_eYear;
	int race_eMonth;
	int race_eDay;
	int race_eHour;
	int race_eMinute;
	int race_eSecond;
	int race_1st_1;
	String race_1stN_1;
	int race_1st_2;
	String race_1stN_2;
	int race_2nd_1;
	String race_2ndN_1;
	int race_2nd_2;
	String race_2ndN_2;
	int race_3rd_1;
	String race_3rdN_1;
	int race_3rd_2;
	String race_3rdN_2;
	double tx_BB_10_20;
	double tx_SS_10_20;
	double tx_BY_10_20;
	double tx_BY_10_30;
	double tx_BY_20_30;
	double tx_3B_123;
	double t1_BB_11_12;
	double t1_SS_11_12;
	double t1_SS_12_11;
	double t1_3B_113;
	double t1_BY_11_12;
	double t1_BY_11_30;
	double t1_BY_12_30;
	double t2_BB_10_21;
	double t2_BB_10_22;
	double t2_SS_10_21;
	double t2_SS_10_22;
	double t2_BY_10_21;
	double t2_BY_10_22;
	double t2_BY_21_22;
	double t2_3B_122;
	double t3_BB_10_20;
	double t3_SS_10_20;
	double t3_BY_10_20;
	double t3_BY_10_31;
	double t3_BY_10_32;
	double t3_BY_20_31;
	double t3_BY_20_32;
	double t3_BY_31_32;
	double t3_3B_12_31;
	double t3_3B_12_32;
}

interface RaceResultReceiveListener {
	public void onReceive(List<RaceResult> listRaceResult);
}

public class parser {
	static RaceResult[] spo1_co_kr = new RaceResult[30];
	static RaceResult[] domerace = new RaceResult[30];
	static RaceResult[] kcycle = new RaceResult[30];
	static RaceResult[] kboat = new RaceResult[30];
	static RaceResult[] race_whole = new RaceResult[70];
	static RaceResult[] race_seoul = new RaceResult[70];
	static RaceResult[] race_jeju = new RaceResult[70];
	static RaceResult[] race_pknu = new RaceResult[70];
	
	static class DownThread extends Thread{
		public void run(){
			try{
				Document doc;

					doc = Jsoup.connect("http://www.spo1.co.kr/race/raceResult.do")
							.timeout(100000).get();
					System.out.println(doc.text());
					Elements eles = doc.select("#wrapper #raceResultVO #container #contents .contents_area .contents_box .race_st tbody tr td");
					int together = 0;
					for (int i = 0; i < 30; i++)
						spo1_co_kr[i] = new RaceResult();
					for (int i = 0; i < eles.size(); i++) {
						if (i % 9 == 0)
							together = 0;
						int k = i == 0 ? 0 : i / 9;
						String current = eles.get(i).text();
						spo1_co_kr[k].race_kind = RaceResult.RACE_KIND_CYCLE;
						switch (i % 9) {
						case 0:
							if (current.contains("광명"))
								spo1_co_kr[k].race_grd = RaceResult.RACE_GRD_CYCLE_GWANGMYEONG;
							else if (current.contains("창원"))
								spo1_co_kr[k].race_grd = RaceResult.RACE_GRD_CYCLE_CHANGWON;
							else if (current.contains("부산"))
								spo1_co_kr[k].race_grd = RaceResult.RACE_GRD_CYCLE_PUSAN;
							spo1_co_kr[k].race_no = toInt(current);
							break;
						case 1:
							// 1등 동착인지 확인
							if (search_num(current, ' ') >= 3) {
								int tmp = getIndex_int(current, 2);
								spo1_co_kr[k].race_1st_1 = current.charAt(0);
								spo1_co_kr[k].race_1st_2 = current.charAt(tmp);
								spo1_co_kr[k].race_1stN_1 = current.substring(2,
										tmp - 1);
								spo1_co_kr[k].race_1stN_2 = current.substring(tmp + 2);
								together = 1;
							}
							// 1등동착 아님.
							else {
								spo1_co_kr[k].race_1st_1 = current.charAt(0);
								spo1_co_kr[k].race_1stN_1 = current.substring(1);
							}
							break;
						case 2:
							if (together == 1)
								break;
							// 2등 동착인지 확인
							if (search_num(current, ' ') >= 3) {
								int tmp = getIndex_int(current, 2);
								spo1_co_kr[k].race_2nd_1 = current.charAt(0);
								spo1_co_kr[k].race_2nd_2 = current.charAt(tmp);
								spo1_co_kr[k].race_2ndN_1 = current.substring(2,
										tmp - 1);
								spo1_co_kr[k].race_2ndN_2 = current.substring(tmp + 2);
								together = 2;
							}
							// 2등동착 아님.
							else {
								spo1_co_kr[k].race_2nd_1 = current.charAt(0);
								spo1_co_kr[k].race_2ndN_1 = current.substring(1);
							}
							break;
						case 3:
							if (together == 2)
								break;
							// 3등 동착인지 확인
							if (search_num(current, ' ') >= 3) {
								int tmp = getIndex_int(current, 2);
								spo1_co_kr[k].race_3rd_1 = current.charAt(0);
								spo1_co_kr[k].race_3rd_2 = current.charAt(tmp);
								spo1_co_kr[k].race_3rdN_1 = current.substring(2,
										tmp - 1);
								spo1_co_kr[k].race_3rdN_2 = current.substring(tmp + 2);
								together = 3;
							}
							// 3등동착 아님.
							else {
								spo1_co_kr[k].race_3rd_1 = current.charAt(0);
								spo1_co_kr[k].race_3rdN_1 = current.substring(1);
							}
							break;
						case 6: // 쌍승
							switch (together) {
							case 0:
								spo1_co_kr[k].tx_SS_10_20 = getSS(current);
								break;
							case 1:
								int tmp = search_index(current, ' ');
								spo1_co_kr[k].t1_SS_11_12 = getSS(current.substring(0,
										tmp));
								spo1_co_kr[k].t1_SS_12_11 = getSS(current
										.substring(tmp + 1));
								break;
							case 2:
								tmp = search_index(current, ' ');
								spo1_co_kr[k].t2_SS_10_21 = getSS(current.substring(0,
										tmp));
								spo1_co_kr[k].t2_SS_10_22 = getSS(current
										.substring(tmp + 1));
								break;
							case 3:
								spo1_co_kr[k].t3_SS_10_20 = getSS(current);
								break;
							}
							break;
						case 7: // 복승
							switch (together) {
							case 0:
								spo1_co_kr[k].tx_BB_10_20 = getSS(current);
								break;
							case 1:
								spo1_co_kr[k].t1_BB_11_12 = getSS(current);
								break;
							case 2:
								int tmp = search_index(current, ' ');
								spo1_co_kr[k].t2_BB_10_21 = getSS(current.substring(0,
										tmp));
								spo1_co_kr[k].t2_BB_10_22 = getSS(current
										.substring(tmp + 1));
								break;
							case 3:
								tmp = search_index(current, ' ');
								spo1_co_kr[k].t3_BB_10_20 = getSS(current);
								break;
							}
							break;
						case 8: // 삼복승
							switch (together) {
							case 0:
								spo1_co_kr[k].tx_3B_123 = getSS(current);
								break;
							case 1:
								spo1_co_kr[k].t1_3B_113 = getSS(current);
								break;
							case 2:
								spo1_co_kr[k].t2_3B_122 = getSS(current);
								break;
							case 3:
								int tmp = search_index(current, ' ');
								spo1_co_kr[k].t3_3B_12_31 = getSS(current.substring(0,
										tmp));
								spo1_co_kr[k].t3_3B_12_32 = getSS(current
										.substring(tmp + 1));
								break;
							}
							break;
						}
					}
					System.out.println("read@@ 1");
					
					doc = Jsoup.connect("http://www.domerace.com/race_info/raceresult.htm").timeout(100000).get();
					System.out.println(doc.html());
					Elements eles_even = doc.select("#content-area .tableA tbody .even td");
					Elements eles_odd = doc.select("#contnet-area .tableA tbody .odd td");
					
					int i;
					for (i = 0; i < 30; i++)
						domerace[i] = new RaceResult();
					for (i = 0; i < eles_even.size(); i++) {
						if (i % 9 == 0)
							together = 0;
						int k = i == 0 ? 0 : i / 9;
						String current = eles_even.get(i).text();
						domerace[k].race_kind = RaceResult.RACE_KIND_CYCLE;
						switch (i % 9) {
						case 0:
							if (current.contains("광명"))
								domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_GWANGMYEONG;
							else if (current.contains("창원"))
								domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_CHANGWON;
							else if (current.contains("부산"))
								domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_PUSAN;
							domerace[k].race_no = toInt(current);
							break;
						case 1:
							// 1등 동착인지 확인
							if (search_num(current, ' ') >= 3) {
								int tmp = getIndex_int(current, 2);
								domerace[k].race_1st_1 = current.charAt(0);
								domerace[k].race_1st_2 = current.charAt(tmp);
								domerace[k].race_1stN_1 = current.substring(2, tmp - 1);
								domerace[k].race_1stN_2 = current.substring(tmp + 2);
								together = 1;
							}
							// 1등동착 아님.
							else {
								domerace[k].race_1st_1 = current.charAt(0);
								domerace[k].race_1stN_1 = current.substring(1);
							}
							break;
						case 2:
							if (together == 1)
								break;
							// 2등 동착인지 확인
							if (search_num(current, ' ') >= 3) {
								int tmp = getIndex_int(current, 2);
								domerace[k].race_2nd_1 = current.charAt(0);
								domerace[k].race_2nd_2 = current.charAt(tmp);
								domerace[k].race_2ndN_1 = current.substring(2, tmp - 1);
								domerace[k].race_2ndN_2 = current.substring(tmp + 2);
								together = 2;
								System.out.println("키아 동착~");
							}
							// 2등동착 아님.
							else {
								domerace[k].race_2nd_1 = current.charAt(0);
								domerace[k].race_2ndN_1 = current.substring(1);
							}
							break;
						case 3:
							if (together == 2)
								break;
							// 3등 동착인지 확인
							if (search_num(current, ' ') >= 3) {
								int tmp = getIndex_int(current, 2);
								domerace[k].race_3rd_1 = current.charAt(0);
								domerace[k].race_3rd_2 = current.charAt(tmp);
								domerace[k].race_3rdN_1 = current.substring(2, tmp - 1);
								domerace[k].race_3rdN_2 = current.substring(tmp + 2);
								together = 3;
							}
							// 3등동착 아님.
							else {
								domerace[k].race_3rd_1 = current.charAt(0);
								domerace[k].race_3rdN_1 = current.substring(1);
							}
							break;
						case 6: // 쌍승
							switch (together) {
							case 0:
								domerace[k].tx_SS_10_20 = getSS(current);
								break;
							case 1:
								int tmp = search_index(current, ' ');
								domerace[k].t1_SS_11_12 = getSS(current.substring(0,
										tmp));
								domerace[k].t1_SS_12_11 = getSS(current
										.substring(tmp + 1));
								break;
							case 2:
								tmp = search_index(current, ' ');
								domerace[k].t2_SS_10_21 = getSS(current.substring(0,
										tmp));
								domerace[k].t2_SS_10_22 = getSS(current
										.substring(tmp + 1));
								break;
							case 3:
								domerace[k].t3_SS_10_20 = getSS(current);
								break;
							}
							break;
						case 7: // 복승
							switch (together) {
							case 0:
								domerace[k].tx_BB_10_20 = getSS(current);
								break;
							case 1:
								domerace[k].t1_BB_11_12 = getSS(current);
								break;
							case 2:
								int tmp = search_index(current, ' ');
								domerace[k].t2_BB_10_21 = getSS(current.substring(0,
										tmp));
								domerace[k].t2_BB_10_22 = getSS(current
										.substring(tmp + 1));
								break;
							case 3:
								tmp = search_index(current, ' ');
								domerace[k].t3_BB_10_20 = getSS(current);
								break;
							}
							break;
						case 8: // 삼복승
							switch (together) {
							case 0:
								domerace[k].tx_3B_123 = getSS(current);
								break;
							case 1:
								domerace[k].t1_3B_113 = getSS(current);
								break;
							case 2:
								domerace[k].t2_3B_122 = getSS(current);
								break;
							case 3:
								int tmp = search_index(current, ' ');
								domerace[k].t3_3B_12_31 = getSS(current.substring(0,
										tmp));
								domerace[k].t3_3B_12_32 = getSS(current
										.substring(tmp + 1));
								break;
							}
							break;
						}
					}

					for (int j = 0; j < eles_odd.size(); j++) {
						int k = (j + i) == 0 ? 0 : (j + i) / 9;
						if (j % 9 == 0)
							together = 0;
						String current = eles_odd.get(j).text();
						domerace[k].race_kind = RaceResult.RACE_KIND_CYCLE;
						switch (i % 9) {
						case 0:
							if (current.contains("광명"))
								domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_GWANGMYEONG;
							else if (current.contains("창원"))
								domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_CHANGWON;
							else if (current.contains("부산"))
								domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_PUSAN;
							domerace[k].race_no = toInt(current);
							break;
						case 1:
							// 1등 동착인지 확인
							if (search_num(current, ' ') >= 3) {
								int tmp = getIndex_int(current, 2);
								domerace[k].race_1st_1 = current.charAt(0);
								domerace[k].race_1st_2 = current.charAt(tmp);
								domerace[k].race_1stN_1 = current.substring(2, tmp - 1);
								domerace[k].race_1stN_2 = current.substring(tmp + 2);
								together = 1;
							}
							// 1등동착 아님.
							else {
								domerace[k].race_1st_1 = current.charAt(0);
								domerace[k].race_1stN_1 = current.substring(1);
							}
							break;
						case 2:
							if (together == 1)
								break;
							// 2등 동착인지 확인
							if (search_num(current, ' ') >= 3) {
								int tmp = getIndex_int(current, 2);
								domerace[k].race_2nd_1 = current.charAt(0);
								domerace[k].race_2nd_2 = current.charAt(tmp);
								domerace[k].race_2ndN_1 = current.substring(2, tmp - 1);
								domerace[k].race_2ndN_2 = current.substring(tmp + 2);
								together = 2;
								System.out.println("키아 동착~");
							}
							// 2등동착 아님.
							else {
								domerace[k].race_2nd_1 = current.charAt(0);
								domerace[k].race_2ndN_1 = current.substring(1);
							}
							break;
						case 3:
							if (together == 2)
								break;
							// 3등 동착인지 확인
							if (search_num(current, ' ') >= 3) {
								int tmp = getIndex_int(current, 2);
								domerace[k].race_3rd_1 = current.charAt(0);
								domerace[k].race_3rd_2 = current.charAt(tmp);
								domerace[k].race_3rdN_1 = current.substring(2, tmp - 1);
								domerace[k].race_3rdN_2 = current.substring(tmp + 2);
								together = 3;
							}
							// 3등동착 아님.
							else {
								domerace[k].race_3rd_1 = current.charAt(0);
								domerace[k].race_3rdN_1 = current.substring(1);
							}
							break;
						case 6: // 쌍승
							switch (together) {
							case 0:
								domerace[k].tx_SS_10_20 = getSS(current);
								break;
							case 1:
								int tmp = search_index(current, ' ');
								domerace[k].t1_SS_11_12 = getSS(current.substring(0,
										tmp));
								domerace[k].t1_SS_12_11 = getSS(current
										.substring(tmp + 1));
								break;
							case 2:
								tmp = search_index(current, ' ');
								domerace[k].t2_SS_10_21 = getSS(current.substring(0,
										tmp));
								domerace[k].t2_SS_10_22 = getSS(current
										.substring(tmp + 1));
								break;
							case 3:
								tmp = search_index(current, ' ');
								domerace[k].t3_SS_10_20 = getSS(current.substring(0,
										tmp));
								domerace[k].t3_SS_10_20 = getSS(current
										.substring(tmp + 1));
								break;
							}
							break;
						case 7: // 복승
							switch (together) {
							case 0:
								domerace[k].tx_BB_10_20 = getSS(current);
								break;
							case 1:
								domerace[k].t1_BB_11_12 = getSS(current);
								break;
							case 2:
								int tmp = search_index(current, ' ');
								domerace[k].t2_BB_10_21 = getSS(current.substring(0,
										tmp));
								domerace[k].t2_BB_10_22 = getSS(current
										.substring(tmp + 1));
								break;
							case 3:
								tmp = search_index(current, ' ');
								domerace[k].t3_BB_10_20 = getSS(current);
								break;
							}
							break;
						case 8: // 삼복승
							switch (together) {
							case 0:
								domerace[k].tx_3B_123 = getSS(current);
								break;
							case 1:
								domerace[k].t1_3B_113 = getSS(current);
								break;
							case 2:
								domerace[k].t2_3B_122 = getSS(current);
								break;
							case 3:
								int tmp = search_index(current, ' ');
								domerace[k].t3_3B_12_31 = getSS(current.substring(0,
										tmp));
								domerace[k].t3_3B_12_32 = getSS(current
										.substring(tmp + 1));
								break;
							}
							break;
						}
					}
					System.out.println("read@@##y");
					doc = Jsoup.connect("http://www.kcycle.or.kr/contents/information/raceResultPage.do")
							.timeout(100000).get();
					eles = doc.select(".playResultLsit tbody tr td");
					for (i = 0; i < 20; i++)
						kcycle[i] = new RaceResult();
					for (i = 0; i < eles.size(); i++) {
						if (i % 10 == 0)
							together = 0;
						int k = i == 0 ? 0 : i / 10;
						String current = eles.get(i).text();
						kcycle[k].race_kind = RaceResult.RACE_KIND_CYCLE;
						switch (i % 10) {
						case 0:
							if (current.contains("광명"))
								kcycle[k].race_grd = RaceResult.RACE_GRD_CYCLE_GWANGMYEONG;
							else if (current.contains("창원"))
								kcycle[k].race_grd = RaceResult.RACE_GRD_CYCLE_CHANGWON;
							else if (current.contains("부산"))
								kcycle[k].race_grd = RaceResult.RACE_GRD_CYCLE_PUSAN;
							kcycle[k].race_no = toInt(current);
							break;
						case 1:
							// 1등 동착인지 확인
							if (current.length() >= 6) {
								int tmp = search_index(current, ' ');
								kcycle[k].race_1st_1 = circletonum(current.charAt(0)
										+ "");
								kcycle[k].race_1st_2 = circletonum(current
										.charAt(tmp + 1) + "");
								kcycle[k].race_1stN_1 = current.substring(1, tmp);
								kcycle[k].race_1stN_2 = current.substring(tmp + 2);
								together = 1;
							}
							// 2등동착 아님.
							else {
								kcycle[k].race_1stN_1 = current.substring(1);
								kcycle[k].race_1st_1 = circletonum(current.charAt(0) + "");
							}
							break;
						case 2:
							if (together == 1)
								break;
							// 2등 동착인지 확인
							System.out.println("2등__" + current);
							if (current.length() >= 6) {
								int tmp = search_index(current, ' ');
								kcycle[k].race_2nd_1 = circletonum(current.charAt(0)
										+ "");
								kcycle[k].race_2nd_2 = circletonum(current
										.charAt(tmp + 1) + "");
								kcycle[k].race_2ndN_1 = current.substring(1, tmp);
								kcycle[k].race_2ndN_2 = current.substring(tmp + 2);
								together = 2;
							}
							// 2등동착 아님.
							else {
								kcycle[k].race_2ndN_1 = current.substring(1);
								kcycle[k].race_2nd_1 = circletonum(current.charAt(0) + "");
							}
							break;
						case 3:
							if(together == 2) break;
							if (current.length() >= 6) {
								int tmp = search_index(current, ' ');
								kcycle[k].race_3rd_1 = circletonum(current.charAt(0)
										+ "");
								kcycle[k].race_3rd_2 = circletonum(current
										.charAt(tmp + 1) + "");
								kcycle[k].race_3rdN_1 = current.substring(1, tmp);
								kcycle[k].race_3rdN_2 = current.substring(tmp + 2);
								together = 3;
							}
							// 2등동착 아님.
							else {
								kcycle[k].race_3rdN_1 = current.substring(1);
								kcycle[k].race_3rd_1 = circletonum(current.charAt(0) + "");
							}
							break;
						case 6: // 쌍승
							switch (together) {
							case 0:
								kcycle[k].tx_SS_10_20 = getSS_end(current);
								break;
							case 1:
								int tmp = search_index(current, ' ');
								kcycle[k].t1_SS_11_12 = getSS_end(current.substring(0,
										tmp));
								kcycle[k].t1_SS_12_11 = getSS_end(current
										.substring(tmp + 1));
								break;
							case 2:
								tmp = search_index(current, ' ');
								kcycle[k].t2_SS_10_21 = getSS_end(current.substring(0,
										tmp));
								kcycle[k].t2_SS_10_22 = getSS_end(current
										.substring(tmp + 1));
								break;
							case 3:
								tmp = search_index(current, ' ');
								kcycle[k].t3_SS_10_20 = getSS_end(current);
								break;
							}
							break;
						case 7: // 복승
							switch (together) {
							case 0:
								kcycle[k].tx_BB_10_20 = getSS_end(current);
								break;
							case 1:
								kcycle[k].t1_BB_11_12 = getSS_end(current);
								break;
							case 2:
								int tmp = search_index(current, ' ');
								kcycle[k].t2_BB_10_21 = getSS_end(current.substring(0,
										tmp));
								kcycle[k].t2_BB_10_22 = getSS_end(current
										.substring(tmp + 1));
								break;
							case 3:
								kcycle[k].t3_BB_10_20 = getSS_end(current);
								break;
							}
							break;
						case 8: // 삼복승
							switch (together) {
							case 0:
								kcycle[k].tx_3B_123 = getSS_end(current);
								break;
							case 1:
								kcycle[k].t1_3B_113 = getSS_end(current);
								break;
							case 2:
								kcycle[k].t2_3B_122 = getSS_end(current);
								break;
							case 3:
								int tmp = search_index(current, ' ');
								kcycle[k].t3_3B_12_31 = getSS_end(current.substring(0,
										tmp));
								kcycle[k].t3_3B_12_32 = getSS_end(current
										.substring(tmp + 1));
								break;
							}
							break;
						}
					}
					System.out.println("CONNECT");
					doc = Jsoup
							.connect(
									"http://www.kboat.or.kr/contents/information/raceResultList.do")
							.timeout(10000).get();
					eles = doc.select(".tb_data4 tbody tr td");
					System.out.println("KK");
					
					for (i = 0; i < 20; i++)
						kboat[i] = new RaceResult();
					for (i = 0; i < 15*11; i++) {
						if (i % 11 == 0)
							together = 0;
						int k = i == 0 ? 0 : i / 11;
						String current = eles.get(i).text();
						kboat[k].race_kind = RaceResult.RACE_KIND_BOAT;
						kboat[k].race_grd = RaceResult.RACE_GRD_BOAT_MISARI;
						switch (i % 11) {
						case 0:
							kboat[k].race_no = toInt(current);
							break;
						case 1:
							// 1등 동착인지 확인
							if (current.length() >= 6) {
								int tmp = search_index(current, ' ');
								kboat[k].race_1st_1 = circletonum(current.charAt(0)
										+ "");
								kboat[k].race_1st_2 = circletonum(current
										.charAt(tmp + 1) + "");
								kboat[k].race_1stN_1 = current.substring(1, tmp);
								kboat[k].race_1stN_2 = current.substring(tmp + 2);
								together = 1;
							}
							// 2등동착 아님.
							else {
								kboat[k].race_1stN_1 = current.substring(1);
								kboat[k].race_1st_1 = circletonum(current.charAt(0) + "");
							}
							break;
						case 2:
							if (together == 1)
								break;
							// 2등 동착인지 확인
							if (current.length() >= 6) {
								int tmp = search_index(current, ' ');
								kboat[k].race_2nd_1 = circletonum(current.charAt(0)
										+ "");
								kboat[k].race_2nd_2 = circletonum(current
										.charAt(tmp + 1) + "");
								kboat[k].race_2ndN_1 = current.substring(1, tmp);
								kboat[k].race_2ndN_2 = current.substring(tmp + 2);
								together = 2;
							}
							// 2등동착 아님.
							else {
								kboat[k].race_2ndN_1 = current.substring(1);
								kboat[k].race_2nd_1 = circletonum(current.charAt(0) + "");
							}
							break;
						case 3:
							if (together == 2)
								break;
							if (current.length() >= 6) {
								int tmp = search_index(current, ' ');
								kboat[k].race_3rd_1 = circletonum(current.charAt(0)
										+ "");
								kboat[k].race_3rd_2 = circletonum(current
										.charAt(tmp + 1) + "");
								kboat[k].race_3rdN_1 = current.substring(1, tmp);
								kboat[k].race_3rdN_2 = current.substring(tmp + 2);
								together = 3;
							}
							// 2등동착 아님.
							else {
								kboat[k].race_3rdN_1 = current.substring(1);
								kboat[k].race_3rd_1 = circletonum(current.charAt(0) + "");
							}
							break;
						case 6: // 쌍승
							switch (together) {
							case 0:
								kboat[k].tx_SS_10_20 = getSS_space(current);
								break;
							case 1:
								int tmp = getIndex_space(current, 2);
								System.out.println("ynanyanynanay"+current);
								System.out.println(tmp+"wthe");
								kboat[k].t1_SS_11_12 = getSS_space(current.substring(0, tmp-1));
								kboat[k].t1_SS_12_11 = getSS_space(current.substring(tmp+1));
								break;
							case 2:
								tmp = getIndex_space(current, 2);
								kboat[k].t2_SS_10_21 = getSS_space(current.substring(0, tmp-1));
								kboat[k].t2_SS_10_22 = getSS_space(current.substring(tmp+1));
								break;
							case 3:
								kboat[k].t3_SS_10_20 = getSS_space(current);
								break;
							}
							break;
						case 7: // 복승
							switch (together) {
							case 0:
								kboat[k].tx_BB_10_20 = getSS_space(current);
								break;
							case 1:
								kboat[k].t1_BB_11_12 = getSS_space(current);
								break;
							case 2:
								int tmp = getIndex_space(current, 2);
								kboat[k].t2_BB_10_21 = getSS_space(current.substring(0, tmp-1));
								kboat[k].t2_BB_10_22 = getSS_space(current.substring(tmp+1));
								break;
							case 3:
								kboat[k].t3_BB_10_20 = getSS_space(current);
								break;
							}
							break;
						case 8: // 삼복승
							switch (together) {
							case 0:
								kboat[k].tx_3B_123 = getSS_space(current);
								break;
							case 1:
								kboat[k].t1_3B_113 = getSS_space(current);
								break;
							case 2:
								kboat[k].t2_3B_122 = getSS_space(current);
								break;
							case 3:
								int tmp = getIndex_space(current, 2);
								kboat[k].t3_3B_12_31 = getSS_space(current.substring(0, tmp-1));
								kboat[k].t3_3B_12_32 = getSS_space(current.substring(tmp+1));
								break;
							}
							break;
						}
					}
					
					
					eles = Jsoup.connect("http://race.kra.co.kr/DynamicCacheProcess.kra?param=ThisWeekScoretableDailyScoretable&meet=0")
							.timeout(100000).referrer("http://race.kra.co.kr/race/Main.jsp").get().select(".brdDataView01 tbody tr td");
					
					for (i= 0; i < 70; i++)
						race_whole[i] = new RaceResult();
					for (i = 0; i < eles.size(); i++) {
						if (i % 10 == 0)
							together = 0;
						int k = i == 0 ? 0 : i / 10;
						String current = eles.get(i).text();
						race_whole[k].race_kind = RaceResult.RACE_KIND_HORSE;
						switch (i % 10) {
						case 0:
							if(current.equals("서울")) race_whole[k].race_grd = RaceResult.RACE_GRD_HORSE_SEOUL;
							else if(current.equals("제주")) race_whole[k].race_grd = RaceResult.RACE_GRD_HORSE_JEJU;
							else if(current.equals("부경")) race_whole[k].race_grd = RaceResult.RACE_GRD_HORSE_PKNU;
							break;
						case 1:
							StringTokenizer st = new StringTokenizer(current, "/");
							race_whole[k].race_eYear = toInt(st.nextToken());
							race_whole[k].race_eMonth = toInt(st.nextToken());
							race_whole[k].race_eDay = toInt(st.nextToken());
							race_whole[k].race_date = new Date(race_whole[k].race_eYear, race_whole[k].race_eMonth, race_whole[k].race_eDay);
							break;
						case 2:
							race_whole[k].race_no = toInt(current);
							break;
						
						case 5: // 복승
							switch (together) {
							case 0:
								System.out.println("도시떼"+current);
								race_whole[k].tx_BB_10_20 = getSS_space(current);
								break;
							case 1:
								race_whole[k].t1_BB_11_12 = getSS_space(current);
								break;
							case 2:
								int tmp = getIndex_space(current, 2);
								race_whole[k].t2_BB_10_21 = getSS_space(current.substring(0, tmp-1));
								race_whole[k].t2_BB_10_22 = getSS_space(current.substring(tmp+1));
								break;
							case 3:
								race_whole[k].t3_BB_10_20 = getSS_space(current);
								break;
							}
							break;
							
						case 6: // 쌍승
							switch (together) {
							case 0:
								race_whole[k].tx_SS_10_20 = getSS_space(current);
								break;
							case 1:
								int tmp = getIndex_space(current, 2);
								race_whole[k].t1_SS_11_12 = getSS_space(current.substring(0, tmp-1));
								race_whole[k].t1_SS_12_11 = getSS_space(current.substring(tmp+1));
								break;
							case 2:
								tmp = getIndex_space(current, 2);
								race_whole[k].t2_SS_10_21 = getSS_space(current.substring(0, tmp-1));
								race_whole[k].t2_SS_10_22 = getSS_space(current.substring(tmp+1));
								break;
							case 3:
								race_whole[k].t3_SS_10_20 = getSS_space(current);
								break;
							}
							break;
						case 7: // 복연승
							double BY_1, BY_2, BY_3;
							int tmp1 = getIndex_space(current, 2);
							int tmp2 = getIndex_space(current, 4);
							BY_1 = getSS_space(current.substring(0, tmp1-1));
							BY_2 = getSS_space(current.substring(tmp1+1, tmp2-1));
							BY_3 = getSS_space(current.substring(tmp2+1));
							switch(together){
							case 0:
								race_whole[k].tx_BY_10_20 = BY_1;
								race_whole[k].tx_BY_10_30 = BY_2;
								race_whole[k].tx_BY_20_30 = BY_3;
								break;
							case 1:
								race_whole[k].t1_BY_11_12 = BY_1;
								race_whole[k].t1_BY_11_30 = BY_2;
								race_whole[k].t1_BY_12_30 = BY_3;
								break;
							case 2:
								race_whole[k].t2_BY_10_21 = BY_1;
								race_whole[k].t2_BY_10_22 = BY_2;
								race_whole[k].t2_BY_21_22 = BY_3;
							case 3:
								race_whole[k].t3_BY_10_20 = BY_1;
								race_whole[k].t3_BY_10_31 = BY_2;
								race_whole[k].t3_BY_10_32 = getSS_space(current.substring(tmp2+1, getIndex_space(current, 6)-1));
								race_whole[k].t3_BY_20_31 = getSS_space(current.substring(getIndex_space(current, 6)+1, getIndex_space(current, 8)-1));
								race_whole[k].t3_BY_20_32 = getSS_space(current.substring(getIndex_space(current, 8)+1, getIndex_space(current, 10)-1));
								race_whole[k].t3_BY_31_32 = getSS_space(current.substring(getIndex_space(current, 10)+1, getIndex_space(current, 12)-1));
							break;
						case 8: // 삼복승
							switch (together) {
							case 0:
								race_whole[k].tx_3B_123 = getSS_space(current);
								break;
							case 1:
								race_whole[k].t1_3B_113 = getSS_space(current);
								break;
							case 2:
								race_whole[k].t2_3B_122 = getSS_space(current);
								break;
							case 3:
								int tmp = getIndex_space(current, 2);
								race_whole[k].t3_3B_12_31 = getSS_space(current.substring(0, tmp-1));
								race_whole[k].t3_3B_12_32 = getSS_space(current.substring(tmp+1));
								break;
							}
							break;
						}
						}
					}
					
					Thread.sleep(10800000);
					
					//http://race.kra.co.kr/race/todayrace/todayrace.jsp?Act=todayrace&Sub=4
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{}
			
		}
	}
	
	public static double toDouble(String str){
		String tmp = "";
		for (int i=0; i<str.length(); i++)
			switch(str.charAt(i)){
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
				tmp += str.charAt(i);
				break;
			}
		return Double.parseDouble(tmp);
	}

	public static int toInt(String str) {
		String tmp = "";
		for (int i = 0; i < str.length(); i++)
			switch (str.charAt(i)) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				tmp += str.charAt(i);
				break;
			}
		return Integer.parseInt(tmp);
	}

	public static int search_num(String main, char sub) {
		int tmp = 0;
		for (int i = 0; i < main.length(); i++)
			if (main.charAt(i) == sub)
				tmp++;
		return tmp;
	}

	public static int search_index(String main, char sub) {
		int tmp = 0;
		for (int i = 0; i < main.length(); i++)
			if (main.charAt(i) == sub)
				tmp = i;
		return tmp;
	}

	public static double getSS(String str) {
		return Double.parseDouble(str.substring(search_index(str, '(') + 1,
				search_index(str, ')')));
	}

	public static double getSS_end(String str) {
		return Double.parseDouble(str.substring(search_index(str, ')')));
	}
	
	public static double getSS_space(String str){
		return toDouble(str.substring(search_index(str, ' ')+1));
	}
	
	public static int getIndex_space(String str, int index){
		int ind = 0;
		for(int i=0; i < str.length(); i++)
			switch(str.charAt(i)){
			case ' ':
				ind ++;
				if(ind == index) return i;
			}
		return -1;
	}

	public static int getIndex_int(String str, int index) {
		int ind = 0;
		int tmp = 0;
		for (int i = 0; i < str.length(); i++)
			switch (str.charAt(i)) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				ind = i;
				tmp++;
				break;
			}
		if (tmp != index)
			return -1;
		else
			return ind;
	}

	public static int circletonum(String circle) {
		if (circle.contains("①"))
			return 1;
		else if (circle.contains("②"))
			return 2;
		else if (circle.contains("③"))
			return 3;
		else if (circle.contains("④"))
			return 4;
		else if (circle.contains("⑤"))
			return 5;
		else if (circle.contains("⑥"))
			return 6;
		else if (circle.contains("⑦"))
			return 7;
		else if (circle.contains("⑧"))
			return 8;
		else if (circle.contains("⑨"))
			return 9;
		else if (circle.contains("⑩"))
			return 10;
		else if (circle.contains("⑪"))
			return 11;
		else if (circle.contains("⑫"))
			return 12;
		else if (circle.contains("⑬"))
			return 13;
		else if (circle.contains("⑭"))
			return 14;
		else
			return 0;
	}

	public static void main(String[] args){
		Thread t = new DownThread();
		t.run();
	}
}