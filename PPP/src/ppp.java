import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class RaceResult {
	public static final int RACE_KIND_HORSE = 0, RACE_KIND_CYCLE = 1, RACE_KIND_BOAT = 2;
	public static final int RACE_GRD_HORSE_GWACHEON = 0, RACE_GRD_HORSE_JEJU = 1, RACE_GRD_HORSE_PUSAN = 2;
	public static final int RACE_GRD_CYCLE_GWANGMYEONG = 0, RACE_GRD_CYCLE_CHANGWON = 1, RACE_GRD_CYCLE_PUSAN = 2;
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

class spo1{
	public String racename;
	public int first_num;
	public String first_name;
	public int second_num;
	public String second_name;
	public int third_num;
	public String third_name;
	public String column1; //´Ü½Â
	public String column2; //¿¬½Â
	public String column3; //½Ö½Â
	public String column4; //º¹½Â
	public String column5; //»ïº¹½Â
}

class race{
	public String location;
	public String date;
	public int racenum;
	public int first_num;
	public int second_num;
	public int third_num;
	public String column1; //´Ü½Â½Ä
	public String column2; //¿¬½Â½Ä
	public String column3; //º¹½Â½Ä
	public String column4; //½Ö½Â½Ä
	public String column5; //º¹¿¬½Â½Ä
	public String column6; //»ïº¹½Â½Ä
}

class DownThread extends Thread{
	public void run(){
		while(true){
			try {
				ppp.Down();
				Thread.sleep(3600000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

public class ppp {
	static RaceResult[] spo1_co_kr = new RaceResult[30];
	static spo1[] page1_data = new spo1[30];
	static spo1[] page2_data = new spo1[30];
	static spo1[] page3_data = new spo1[30];
	static spo1[] page4_data = new spo1[30];
	static race[] seoul_data = new race[70];
	static race[] pknu_data = new race[70];
	static race[] whole_data = new race[70];
	static race[] jeju_data = new race[70];
	
	static String page1_source;
	static String page2_source;
	static String page3_source;
	static String page4_source;
	static String seoul_source;
	
	static void Down(){
		Document doc;
		try {
			//page1;;;
			doc = Jsoup.connect("http://www.spo1.co.kr/race/raceResult.do").get();
			Elements eles = doc.select("#wrapper #raceResultVO #container #contents .contens_area .contents_box .race_st tbody tr td");
			
			if(eles.text().contains("°æÁÖ °á°ú°¡ ¾ø½À´Ï´Ù"));
			else if(page1_source == eles.text());
			else{
			page1_source = eles.text();
			
			
			for(int i=0; i<30; i++) page1_data[i] = new spo1();
			for(int i=0; i<30; i++) spo1_co_kr[i] = new RaceResult();
			for(int i=0; i<eles.size(); i++){
				//System.out.println(eles.get(i).text().length());
				int k = i==0? 0 : i/9;
				spo1_co_kr[k].race_kind = RaceResult.RACE_KIND_CYCLE;
				String current = eles.get(i).text();
				switch(i%9){
				case 0:
					if (current.contains("±¤¸í")) spo1_co_kr[k].race_grd = RaceResult.RACE_GRD_CYCLE_GWANGMYEONG;
					else if (current.contains("Ã¢¿ø")) spo1_co_kr[k].race_grd = RaceResult.RACE_GRD_CYCLE_CHANGWON;
					else if (current.contains("ºÎ»ê")) spo1_co_kr[k].race_grd = RaceResult.RACE_GRD_CYCLE_PUSAN;
					break;
				case 1:
					if(eles.get(i).text().length()!=0){
					page1_data[k].first_name = eles.get(i).text().substring(2);
					page1_data[k].first_num = Integer.parseInt(eles.get(i).text().charAt(0)+"");
					}
					break;
				case 2:
					if(eles.get(i).text().length()!=0){
					page1_data[k].second_name = eles.get(i).text().substring(2);
					page1_data[k].second_num = Integer.parseInt(eles.get(i).text().charAt(0)+"");
					}
					break;
				case 3:
					if(eles.get(i).text().length()!=0){
					page1_data[k].third_name = eles.get(i).text().substring(2);
					page1_data[k].third_num = Integer.parseInt(eles.get(i).text().charAt(0)+"");
					}
					break;
				case 4:
					page1_data[k].column1 = eles.get(i).text();
					break;
				case 5:
					page1_data[k].column2 = eles.get(i).text();
					break;
				case 6:
					page1_data[k].column3 = eles.get(i).text();
					break;
				case 7:
					page1_data[k].column4 = eles.get(i).text();
					break;
				case 8:
					page1_data[k].column5 = eles.get(i).text();
					break;
				}
			}
			int t=0;
			System.out.println(t+": "+page1_data[t].racename+page1_data[t].first_name+page1_data[t].first_num+page1_data[t].second_name+page1_data[t].second_num+page1_data[t].third_name+page1_data[t].third_num+page1_data[t].column1+page1_data[t].column2+page1_data[t].column3+page1_data[t].column4+page1_data[t].column5);
			}
			
			
			doc = Jsoup.connect("http://www.domerace.com/race_info/raceresult.htm").get();
			if(eles.text().contains("°æÁÖ °á°ú°¡ ¾ø½À´Ï´Ù"));
			else if(page2_source == eles.text());
			else{
			page2_source = eles.text();
			
			Elements eles_even = doc.select("#content-area .tableA tbody .even td");
			Elements eles_odd = doc.select("#content-area .tableA tbody .odd td");
			int i;
			
			for(i=0; i<30; i++) page2_data[i] = new spo1();
			for(i=0; i<eles_even.size(); i++){
				System.out.println("2: "+ eles_even.get(i).text());
				int k = i==0? 0 : i/9;
				switch(i%9){
				case 0:
					page2_data[k].racename = eles_even.get(i).text();
					break;
				case 1:
					if(eles_even.get(i).text().length()!=0){
					page2_data[k].first_name = eles_even.get(i).text().substring(2);
					page2_data[k].first_num = Integer.parseInt(eles_even.get(i).text().charAt(0)+"");
					}
					break;
				case 2:
					if(eles_even.get(i).text().length()!=0){
					page2_data[k].second_name = eles_even.get(i).text().substring(2);
					page2_data[k].second_num = Integer.parseInt(eles_even.get(i).text().charAt(0)+"");
					}
					break;
				case 3:
					if(eles_even.get(i).text().length()!=0){
					page2_data[k].third_name = eles_even.get(i).text().substring(2);
					page2_data[k].third_num = Integer.parseInt(eles_even.get(i).text().charAt(0)+"");
					}
					break;
				case 4:
					page2_data[k].column1 = eles_even.get(i).text();
					break;
				case 5:
					page2_data[k].column2 = eles_even.get(i).text();
					break;
				case 6:
					page2_data[k].column3 = eles_even.get(i).text();
					break;
				case 7:
					page2_data[k].column4 = eles_even.get(i).text();
					break;
				case 8:
					page2_data[k].column5 = eles_even.get(i).text();
					break;
				}
			}
			
			for(int j=0; j<eles_odd.size(); j++){
				System.out.println(eles_odd.get(j).text());
				int k = (j+i)==0? 0 : (j+i)/9;
				switch(j%9){
				case 0:
					page2_data[k].racename = eles_odd.get(j).text();
					break;
				case 1:
					if(eles_odd.get(j).text().length()!=0){
					page2_data[k].first_name = eles_odd.get(j).text().substring(2);
					page2_data[k].first_num = Integer.parseInt(eles_odd.get(j).text().charAt(0)+"");
					}
					break;
				case 2:
					if(eles_odd.get(j).text().length()!=0){
					page2_data[k].second_name = eles_odd.get(j).text().substring(2);
					page2_data[k].second_num = Integer.parseInt(eles_odd.get(j).text().charAt(0)+"");
					}
					break;
				case 3:
					if(eles_odd.get(j).text().length()!=0){
					page2_data[k].third_name = eles_odd.get(j).text().substring(2);
					page2_data[k].third_num = Integer.parseInt(eles_odd.get(j).text().charAt(0)+"");
					}
					break;
				case 4:
					page2_data[k].column1 = eles_odd.get(j).text();
					break;
				case 5:
					page2_data[k].column2 = eles_odd.get(j).text();
					break;
				case 6:
					page2_data[k].column3 = eles_odd.get(j).text();
					break;
				case 7:
					page2_data[k].column4 = eles_odd.get(j).text();
					break;
				case 8:
					page2_data[k].column5 = eles_odd.get(j).text();
					break;
				}
			}
			int t=0;
			System.out.println(t+": "+page2_data[t].racename+page2_data[t].first_name+page2_data[t].first_num+page2_data[t].second_name+page2_data[t].second_num+page2_data[t].third_name+page2_data[t].third_num+page2_data[t].column1+page2_data[t].column2+page2_data[t].column3+page2_data[t].column4+page2_data[t].column5);
			}
			
			
			
			/***
			 * °æÁÖ±¸ºÐ
			 * 1µî ¸»¹øÈ£
			 * 1µî ±â¼ö¸í
			 * 2µî ¸»¹øÈ£
			 * 2µî ±â¼ö¸í
			 * 3µî ¸»¹øÈ£
			 * 3µî ±â¼ö¸í
			 * º¹½Â½Ä
			 * ½Ö½Â½Ä
			 * º¹½Â¤¾½Ä
			 * »ïº¹½Â½Ä
			 */
			
			doc = Jsoup.connect("http://www.kcycle.or.kr/contents/information/raceResultPage.do").get();
			eles = doc.select(".playResultList tbody tr td");
			
			for(int i=0; i<20; i++) page3_data[i] = new spo1();
			for(int i=0; i<eles.size(); i++){
				int k = i==0? 0 : i/10;
				switch(i%10){
				case 0:
					page3_data[k].racename = eles.get(i).text();
					break;
				case 1:
					if(eles.get(i).text().length()!=0){
					page3_data[k].first_name = eles.get(i).text().substring(1);
					page3_data[k].first_num = circletonum(eles.get(i).text().charAt(0)+"");
					}
					break;
				case 2:
					if(eles.get(i).text().length()!=0){
					page3_data[k].second_name = eles.get(i).text().substring(1);
					page3_data[k].second_num = circletonum(eles.get(i).text().charAt(0)+"");
					}
					break;
				case 3:
					if(eles.get(i).text().length()!=0){
					page3_data[k].third_name = eles.get(i).text().substring(1);
					page3_data[k].third_num = circletonum(eles.get(i).text().charAt(0)+"");
					}
					break;
				case 4:
					page3_data[k].column1 = eles.get(i).text();
					break;
				case 5:
					page3_data[k].column2 = eles.get(i).text();
					break;
				case 6:
					page3_data[k].column3 = eles.get(i).text();
					break;
				case 7:
					page3_data[k].column4 = eles.get(i).text();
					break;
				case 8:
					page3_data[k].column5 = eles.get(i).text();
					break;
				}
			}
			int t= 0;
			System.out.println(t+": "+page3_data[t].racename+"ff"+page3_data[t].first_name+"ff"+page3_data[t].first_num+"ff"+page3_data[t].second_name+"ff"+page3_data[t].second_num+"ff"+page3_data[t].third_name+"ff"+page3_data[t].third_num+page3_data[t].column1+page3_data[t].column2+page3_data[t].column3+page3_data[t].column4+page3_data[t].column5);
			
			doc = Jsoup.connect("http://www.kboat.or.kr/contents/information/raceResultList.do").get();
			
			eles = doc.select(".tb_data4 tbody tr td");
			
			for(int i=0; i<20; i++) page4_data[i] = new spo1();
			for(int i=0; i<15*11; i++){
				System.out.println(eles.get(i).text());
				int k = i==0? 0 : i/11;
				switch(i%11){
				case 0:
					page4_data[k].racename = eles.get(i).text();
					break;
				case 1:
					if(eles.get(i).text().length()!=0){
					page4_data[k].first_name = eles.get(i).text().substring(1);
					page4_data[k].first_num = circletonum(eles.get(i).text().charAt(0)+"");
					}
					break;
				case 2:
					if(eles.get(i).text().length()!=0){
					page4_data[k].second_name = eles.get(i).text().substring(1);
					page4_data[k].second_num = circletonum(eles.get(i).text().charAt(0)+"");
					}
					break;
				case 3:
					if(eles.get(i).text().length()!=0){
					page4_data[k].third_name = eles.get(i).text().substring(1);
					page4_data[k].third_num = circletonum(eles.get(i).text().charAt(0)+"");
					}
					break;
				case 4:
					//page4_data[k].column1 = circletonum( eles.get(i).text().charAt(0)+"")+"";
					//page4_data[k].column1 += Double.parseDouble(eles.get(i).text().substring(2));
					//System.out.println("pls"+eles.get(i).text().substring(0, eles.get(i).text().length()-2));
					//System.out.println(a);
					page4_data[k].column1 =  getFloatINString(eles.get(i).text()).substring(1);
					break;
				case 5:
					//page4_data[k].column2 = circletonum(eles.get(i).text().charAt(0)+"")+"";
					//page4_data[k].column2 += Double.parseDouble(eles.get(i).text().substring(2));
					//System.out.println(eles.get(i).text().indexOf("&nbsp;"));
					page4_data[k].column2 =  getFloatINString(eles.get(i).text()).substring(1);
					break;
				case 6:
					//page4_data[k].column3 = circletonum(eles.get(i).text().charAt(0)+"")+"";
					//page4_data[k].column3 += Double.parseDouble(eles.get(i).text().substring(2));
					//System.out.println("pls"+eles.get(i).text().substring(0, eles.get(i).text().length()-2));
					page4_data[k].column3 =  getFloatINString(eles.get(i).text()).substring(1);
					break;
				case 7:
					//page4_data[k].column4 = circletonum(eles.get(i).text().charAt(0)+"")+"";
					//page4_data[k].column4 += Double.parseDouble(eles.get(i).text().substring(2));
					//System.out.println("pls"+eles.get(i).text().substring(0, eles.get(i).text().length()-2));
					page4_data[k].column4 =  getFloatINString(eles.get(i).text()).substring(1);
					break;
				case 8:
					//page4_data[k].column5 = page4_data[k].column1 = circletonum(eles.get(i).text().charAt(0)+"")+"";
					//page4_data[k].column5 += Double.parseDouble(eles.get(i).text().substring(2));
					//System.out.println("pls"+eles.get(i).text().substring(0, eles.get(i).text().length()-2));
					page4_data[k].column5 =  getFloatINString(eles.get(i).text()).substring(1);
					break;
				}
			}
			
			System.out.println(t+": "+page4_data[t].racename+"ff"+page4_data[t].first_name+"ff"+page4_data[t].first_num+"ff"+page4_data[t].second_name+"ff"+page4_data[t].second_num+"ff"+page4_data[t].third_name+"ff"+page4_data[t].third_num+page4_data[t].column1+page4_data[t].column2+page4_data[t].column3+page4_data[t].column4+page4_data[t].column5);
			
			Connection conn = Jsoup.connect("http://race.kra.co.kr/DynamicCacheProcess.kra?param=ThisWeekScoretableDailyScoretable&meet=1");
			conn.referrer("http://race.kra.co.kr/race/Main.jsp");
			eles = conn.get().select(".brdDataView01 tbody tr td");
			
			for(int i=0; i<70; i++) seoul_data[i] = new race();
			for(int i=0; i<eles.size(); i++){
				int k = i==0? 0 : i/10;
				System.out.println(eles.get(i).text());
				switch(i%10){
				case 0:
					seoul_data[k].location = eles.get(i).text();
					break;
				case 1:
					seoul_data[k].date = eles.get(i).text();
					break;
				case 2:
					seoul_data[k].racenum = Integer.parseInt(eles.get(i).text());
					break;
				case 3:
					seoul_data[k].column1 = eles.get(i).text();
					break;
				case 4:
					seoul_data[k].column2 = eles.get(i).text();
					break;
				case 5:
					seoul_data[k].column3 = eles.get(i).text();
					break;
				case 6:
					seoul_data[k].column4 = eles.get(i).text();
					break;
				case 7:
					seoul_data[k].column5 = eles.get(i).text();
					break;
				case 8:
					seoul_data[k].column6 = eles.get(i).text();
					break;
				}
			}
			
			conn = Jsoup.connect("http://race.kra.co.kr/DynamicCacheProcess.kra?param=ThisWeekScoretableDailyScoretable&meet=2");
			conn.referrer("http://race.kra.co.kr/race/Main.jsp");
			eles = conn.get().select(".brdDataView01 tbody tr td");
			
			for(int i=0; i<70; i++) jeju_data[i] = new race();
			for(int i=0; i<eles.size(); i++){
				int k = i==0? 0 : i/10;
				System.out.println(eles.get(i).text());
				switch(i%10){
				case 0:
					jeju_data[k].location = eles.get(i).text();
					break;
				case 1:
					jeju_data[k].date = eles.get(i).text();
					break;
				case 2:
					jeju_data[k].racenum = Integer.parseInt(eles.get(i).text());
					break;
				case 3:
					jeju_data[k].column1 = eles.get(i).text();
					break;
				case 4:
					jeju_data[k].column2 = eles.get(i).text();
					break;
				case 5:
					jeju_data[k].column3 = eles.get(i).text();
					break;
				case 6:
					jeju_data[k].column4 = eles.get(i).text();
					break;
				case 7:
					jeju_data[k].column5 = eles.get(i).text();
					break;
				case 8:
					jeju_data[k].column6 = eles.get(i).text();
					break;
				}
			}
			
			conn = Jsoup.connect("http://race.kra.co.kr/DynamicCacheProcess.kra?param=ThisWeekScoretableDailyScoretable&meet=3");
			conn.referrer("http://race.kra.co.kr/race/Main.jsp");
			eles = conn.get().select(".brdDataView01 tbody tr td");
			
			for(int i=0; i<70; i++) pknu_data[i] = new race();
			for(int i=0; i<eles.size(); i++){
				int k = i==0? 0 : i/10;
				System.out.println(eles.get(i).text());
				switch(i%10){
				case 0:
					pknu_data[k].location = eles.get(i).text();
					break;
				case 1:
					pknu_data[k].date = eles.get(i).text();
					break;
				case 2:
					pknu_data[k].racenum = Integer.parseInt(eles.get(i).text());
					break;
				case 3:
					pknu_data[k].column1 = eles.get(i).text();
					break;
				case 4:
					pknu_data[k].column2 = eles.get(i).text();
					break;
				case 5:
					pknu_data[k].column3 = eles.get(i).text();
					break;
				case 6:
					pknu_data[k].column4 = eles.get(i).text();
					break;
				case 7:
					pknu_data[k].column5 = eles.get(i).text();
					break;
				case 8:
					pknu_data[k].column6 = eles.get(i).text();
					break;
				}
			}
			
			conn = Jsoup.connect("http://race.kra.co.kr/DynamicCacheProcess.kra?param=ThisWeekScoretableDailyScoretable&meet=0");
			conn.referrer("http://race.kra.co.kr/race/Main.jsp");
			eles = conn.get().select(".brdDataView01 tbody tr td");
			
			for(int i=0; i<70; i++) whole_data[i] = new race();
			for(int i=0; i<eles.size(); i++){
				int k = i==0? 0 : i/10;
				System.out.println(eles.get(i).text());
				switch(i%10){
				case 0:
					whole_data[k].location = eles.get(i).text();
					break;
				case 1:
					whole_data[k].date = eles.get(i).text();
					break;
				case 2:
					whole_data[k].racenum = Integer.parseInt(eles.get(i).text());
					break;
				case 3:
					whole_data[k].column1 = eles.get(i).text();
					break;
				case 4:
					whole_data[k].column2 = eles.get(i).text();
					break;
				case 5:
					whole_data[k].column3 = eles.get(i).text();
					break;
				case 6:
					whole_data[k].column4 = eles.get(i).text();
					break;
				case 7:
					whole_data[k].column5 = eles.get(i).text();
					break;
				case 8:
					whole_data[k].column6 = eles.get(i).text();
					break;
				}
			}
			
			/*
			http://race.kra.co.kr/race/todayrace/todayrace.jsp?Act=todayrace&Sub=4
			*/
			
		} catch (IOException e) {
			//System.out.println("´Ù½Ã ½ÇÇà½ÃÄÑÁÖ¼¼¿ä");
		}
	}
	
	public static int circletonum(String circle){
		if(circle.contains("¨ç")) return 1;
		else if(circle.contains("¨è")) return 2;
		else if(circle.contains("¨é")) return 3;
		else if(circle.contains("¨ê")) return 4;
		else if(circle.contains("¨ë")) return 5;
		else if(circle.contains("¨ì")) return 6;
		else if(circle.contains("¨í")) return 7;
		else if(circle.contains("¨î")) return 8;
		else if(circle.contains("¨ï")) return 9;
		else if(circle.contains("¨ð")) return 10;
		else if(circle.contains("¨ñ")) return 11;
		else if(circle.contains("¨ò")) return 12;
		else if(circle.contains("¨ó")) return 13;
		else if(circle.contains("¨ô")) return 14;
		
		else return 0;
	}
	
	public static String getFloatINString(String str){
		String FLOAT = "";
		int index = 0;
		for(int i=0; i<str.length(); i++){
			switch(str.charAt(i)){
			case '¨ç':
			case '¨è':
			case '¨é':
			case '¨ê':
			case '¨ë':
			case '¨ì':
			case '¨í':
			case '¨î':
			case '¨ï':
			case '¨ð':
			case '¨ñ':
			case '¨ò':
			case '¨ó':
			case '¨ô':
			case '0':
				FLOAT += " "+str.charAt(i);
				break;
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
				FLOAT += str.charAt(i);
				break;
				
			case ' ':
				FLOAT += " ";
			}
		}
		return FLOAT;
	}
	
	public static void main(String[] args){
		Thread t = new DownThread();
		t.run();
		
		
	}
}
