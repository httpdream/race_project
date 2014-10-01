import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;




public class Parser {
	static RaceResult[] spo1_co_kr = new RaceResult[30];
	static RaceResult[] domerace = new RaceResult[30];
	static RaceResult[] kcycle = new RaceResult[30];
	
	public static int toInt(String str){
		String tmp = "";
		for(int i=0; i<str.length(); i++)
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
				tmp += str.charAt(i);
				break;
			}
		return Integer.parseInt(tmp);
	}
	
	public static int search_num(String main, char sub){
		int tmp = 0;
		for(int i=0; i<main.length(); i++)
			if(main.charAt(i) == sub) tmp++;
		return tmp;
	}
	
	public static int search_index(String main, char sub){
		int tmp = 0;
		for(int i=0; i<main.length(); i++)
			if(main.charAt(i) == sub) tmp = i;
		return tmp;
	}
	
	public static double getSS(String str){
		return Double.parseDouble(str.substring(search_index(str, '(')+1, search_index(str, ')')));
	}
	
	public static int getIndex_int(String str, int index){
		int ind = 0;
		int tmp = 0;
		for(int i=0; i<str.length(); i++)
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
				ind = i;
				tmp++;
				break;
			}
		if(tmp!=index) return -1;
		else return ind;
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
	
	public static void main(String[] args) throws IOException{
		Document doc;
		try {
			//page1;;;
			doc = Jsoup.connect("http://www.spo1.co.kr/race/raceResult.do").get();
			Elements eles = doc.select("#wrapper #raceResultVO #container #contents .contens_area .contents_box .race_st tbody tr td");
			
			int together = 0;
			
			for(int i=0; i<30; i++) spo1_co_kr[i] = new RaceResult();
			for(int i=0; i<eles.size(); i++){
				if(i%9 == 0) together = 0;
				int k = i==0? 0 : i/9;
				String current = eles.get(i).text();
				spo1_co_kr[k].race_kind = RaceResult.RACE_KIND_CYCLE;
				switch(i%9){
				case 0:
					if (current.contains("±¤¸í")) spo1_co_kr[k].race_grd = RaceResult.RACE_GRD_CYCLE_GWANGMYEONG;
					else if (current.contains("Ã¢¿ø")) spo1_co_kr[k].race_grd = RaceResult.RACE_GRD_CYCLE_CHANGWON;
					else if (current.contains("ºÎ»ê")) spo1_co_kr[k].race_grd = RaceResult.RACE_GRD_CYCLE_PUSAN;
					spo1_co_kr[k].race_no = toInt(current); 
					break;
				case 1:
					//1µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						spo1_co_kr[k].race_1st_1 = current.charAt(0);
						spo1_co_kr[k].race_1st_2 = current.charAt(tmp);
						spo1_co_kr[k].race_1stN_1 = current.substring(2, tmp-1);
						spo1_co_kr[k].race_1stN_2 = current.substring(tmp+2);
						together = 1;
					}
					//1µîµ¿Âø ¾Æ´Ô.
					else{
						spo1_co_kr[k].race_1st_1 = current.charAt(0);
						spo1_co_kr[k].race_1stN_1 = current.substring(1);
					}
					break;
				case 2:
					if(together == 1) break;
					//2µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						spo1_co_kr[k].race_2nd_1 = current.charAt(0);
						spo1_co_kr[k].race_2nd_2 = current.charAt(tmp);
						spo1_co_kr[k].race_2ndN_1 = current.substring(2, tmp-1);
						spo1_co_kr[k].race_2ndN_2 = current.substring(tmp+2);
						together = 2;
					}
					//2µîµ¿Âø ¾Æ´Ô.
					else{
						spo1_co_kr[k].race_2nd_1 = current.charAt(0);
						spo1_co_kr[k].race_2ndN_1 = current.substring(1);
					}
					break;
				case 3:
					if(together == 2) break;
					//3µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						spo1_co_kr[k].race_3rd_1 = current.charAt(0);
						spo1_co_kr[k].race_3rd_2 = current.charAt(tmp);
						spo1_co_kr[k].race_3rdN_1 = current.substring(2, tmp-1);
						spo1_co_kr[k].race_3rdN_2 = current.substring(tmp+2);
						together = 3;
					}
					//3µîµ¿Âø ¾Æ´Ô.
					else{
						spo1_co_kr[k].race_3rd_1 = current.charAt(0);
						spo1_co_kr[k].race_3rdN_1 = current.substring(1);
					}
					break;
				case 6: //½Ö½Â
					switch(together){
					case 0:
						spo1_co_kr[k].tx_SS_10_20 = getSS(current);
						break;
					case 1:
						int tmp = search_index(current, ' ');
						spo1_co_kr[k].t1_SS_11_12 = getSS(current.substring(0, tmp));
						spo1_co_kr[k].t1_SS_12_11 = getSS(current.substring(tmp+1));
						break;
					case 2: 
						tmp = search_index(current, ' ');
						spo1_co_kr[k].t2_SS_10_21 = getSS(current.substring(0, tmp));
						spo1_co_kr[k].t2_SS_10_22 = getSS(current.substring(tmp+1));
						break;
						
					case 3:
						tmp = search_index(current, ' ');
						spo1_co_kr[k].t3_SS_10_20 = getSS(current.substring(0, tmp));
						spo1_co_kr[k].t3_SS_10_20 = getSS(current.substring(tmp+1));
						break;
					}
					break;
				case 7: //º¹½Â
					switch(together){
					case 0:
						spo1_co_kr[k].tx_BB_10_20 = getSS(current);
						break;
					case 1:
						spo1_co_kr[k].t1_BB_11_12 = getSS(current);
						break;
					case 2: 
						int tmp = search_index(current, ' ');
						spo1_co_kr[k].t2_BB_10_21 = getSS(current.substring(0, tmp));
						spo1_co_kr[k].t2_BB_10_22 = getSS(current.substring(tmp+1));
						break;
						
					case 3:
						tmp = search_index(current, ' ');
						spo1_co_kr[k].t3_BB_10_20 = getSS(current);
						break;
					}
					break;
				case 8: //»ïº¹½Â
					switch(together){
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
						spo1_co_kr[k].t3_3B_12_31 = getSS(current.substring(0, tmp));
						spo1_co_kr[k].t3_3B_12_32 = getSS(current.substring(tmp+1));
						break;
					}
					break;
				}
			}
			
			Elements eles_even = doc.select("#content-area .tableA tbody .even td");
			Elements eles_odd = doc.select("#content-area .tableA tbody .odd td");
			int i;
			
			for(i=0; i<30; i++) domerace[i] = new RaceResult();	
			for(i=0; i<eles_even.size(); i++){
				if(i%9 == 0) together = 0;
				int k = i==0? 0 : i/9;
				String current = eles_even.get(i).text();
				domerace[k].race_kind = RaceResult.RACE_KIND_CYCLE;
				switch(i%9){
				case 0:
					if (current.contains("±¤¸í")) domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_GWANGMYEONG;
					else if (current.contains("Ã¢¿ø")) domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_CHANGWON;
					else if (current.contains("ºÎ»ê")) domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_PUSAN;
					domerace[k].race_no = toInt(current); 
					break;
				case 1:
					//1µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						domerace[k].race_1st_1 = current.charAt(0);
						domerace[k].race_1st_2 = current.charAt(tmp);
						domerace[k].race_1stN_1 = current.substring(2, tmp-1);
						domerace[k].race_1stN_2 = current.substring(tmp+2);
						together = 1;
					}
					//1µîµ¿Âø ¾Æ´Ô.
					else{
						domerace[k].race_1st_1 = current.charAt(0);
						domerace[k].race_1stN_1 = current.substring(1);
					}
					break;
				case 2:
					if(together == 1) break;
					//2µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						domerace[k].race_2nd_1 = current.charAt(0);
						domerace[k].race_2nd_2 = current.charAt(tmp);
						domerace[k].race_2ndN_1 = current.substring(2, tmp-1);
						domerace[k].race_2ndN_2 = current.substring(tmp+2);
						together = 2;
						System.out.println("Å°¾Æ µ¿Âø~");
					}
					//2µîµ¿Âø ¾Æ´Ô.
					else{
						domerace[k].race_2nd_1 = current.charAt(0);
						domerace[k].race_2ndN_1 = current.substring(1);
					}
					break;
				case 3:
					if(together == 2) break;
					//3µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						domerace[k].race_3rd_1 = current.charAt(0);
						domerace[k].race_3rd_2 = current.charAt(tmp);
						domerace[k].race_3rdN_1 = current.substring(2, tmp-1);
						domerace[k].race_3rdN_2 = current.substring(tmp+2);
						together = 3;
					}
					//3µîµ¿Âø ¾Æ´Ô.
					else{
						domerace[k].race_3rd_1 = current.charAt(0);
						domerace[k].race_3rdN_1 = current.substring(1);
					}
					break;
				case 6: //½Ö½Â
					switch(together){
					case 0:
						domerace[k].tx_SS_10_20 = getSS(current);
						break;
					case 1:
						int tmp = search_index(current, ' ');
						domerace[k].t1_SS_11_12 = getSS(current.substring(0, tmp));
						domerace[k].t1_SS_12_11 = getSS(current.substring(tmp+1));
						break;
					case 2: 
						tmp = search_index(current, ' ');
						domerace[k].t2_SS_10_21 = getSS(current.substring(0, tmp));
						domerace[k].t2_SS_10_22 = getSS(current.substring(tmp+1));
						break;
						
					case 3:
						tmp = search_index(current, ' ');
						domerace[k].t3_SS_10_20 = getSS(current.substring(0, tmp));
						domerace[k].t3_SS_10_20 = getSS(current.substring(tmp+1));
						break;
					}
					break;
				case 7: //º¹½Â
					switch(together){
					case 0:
						domerace[k].tx_BB_10_20 = getSS(current);
						break;
					case 1:
						domerace[k].t1_BB_11_12 = getSS(current);
						break;
					case 2: 
						int tmp = search_index(current, ' ');
						domerace[k].t2_BB_10_21 = getSS(current.substring(0, tmp));
						domerace[k].t2_BB_10_22 = getSS(current.substring(tmp+1));
						break;
						
					case 3:
						tmp = search_index(current, ' ');
						domerace[k].t3_BB_10_20 = getSS(current);
						break;
					}
					break;
				case 8: //»ïº¹½Â
					switch(together){
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
						domerace[k].t3_3B_12_31 = getSS(current.substring(0, tmp));
						domerace[k].t3_3B_12_32 = getSS(current.substring(tmp+1));
						break;
					}
					break;
				}
			}
			
			
			for(int j=0; j<eles_odd.size(); j++){
				int k = (j+i)==0? 0 : (j+i)/9;
				if(j%9 == 0) together = 0;
				String current = eles_odd.get(j).text();
				domerace[k].race_kind = RaceResult.RACE_KIND_CYCLE;
				switch(i%9){
				case 0:
					if (current.contains("±¤¸í")) domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_GWANGMYEONG;
					else if (current.contains("Ã¢¿ø")) domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_CHANGWON;
					else if (current.contains("ºÎ»ê")) domerace[k].race_grd = RaceResult.RACE_GRD_CYCLE_PUSAN;
					domerace[k].race_no = toInt(current); 
					break;
				case 1:
					//1µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						domerace[k].race_1st_1 = current.charAt(0);
						domerace[k].race_1st_2 = current.charAt(tmp);
						domerace[k].race_1stN_1 = current.substring(2, tmp-1);
						domerace[k].race_1stN_2 = current.substring(tmp+2);
						together = 1;
					}
					//1µîµ¿Âø ¾Æ´Ô.
					else{
						domerace[k].race_1st_1 = current.charAt(0);
						domerace[k].race_1stN_1 = current.substring(1);
					}
					break;
				case 2:
					if(together == 1) break;
					//2µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						domerace[k].race_2nd_1 = current.charAt(0);
						domerace[k].race_2nd_2 = current.charAt(tmp);
						domerace[k].race_2ndN_1 = current.substring(2, tmp-1);
						domerace[k].race_2ndN_2 = current.substring(tmp+2);
						together = 2;
						System.out.println("Å°¾Æ µ¿Âø~");
					}
					//2µîµ¿Âø ¾Æ´Ô.
					else{
						domerace[k].race_2nd_1 = current.charAt(0);
						domerace[k].race_2ndN_1 = current.substring(1);
					}
					break;
				case 3:
					if(together == 2) break;
					//3µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						domerace[k].race_3rd_1 = current.charAt(0);
						domerace[k].race_3rd_2 = current.charAt(tmp);
						domerace[k].race_3rdN_1 = current.substring(2, tmp-1);
						domerace[k].race_3rdN_2 = current.substring(tmp+2);
						together = 3;
					}
					//3µîµ¿Âø ¾Æ´Ô.
					else{
						domerace[k].race_3rd_1 = current.charAt(0);
						domerace[k].race_3rdN_1 = current.substring(1);
					}
					break;
				case 6: //½Ö½Â
					switch(together){
					case 0:
						domerace[k].tx_SS_10_20 = getSS(current);
						break;
					case 1:
						int tmp = search_index(current, ' ');
						domerace[k].t1_SS_11_12 = getSS(current.substring(0, tmp));
						domerace[k].t1_SS_12_11 = getSS(current.substring(tmp+1));
						break;
					case 2: 
						tmp = search_index(current, ' ');
						domerace[k].t2_SS_10_21 = getSS(current.substring(0, tmp));
						domerace[k].t2_SS_10_22 = getSS(current.substring(tmp+1));
						break;
						
					case 3:
						tmp = search_index(current, ' ');
						domerace[k].t3_SS_10_20 = getSS(current.substring(0, tmp));
						domerace[k].t3_SS_10_20 = getSS(current.substring(tmp+1));
						break;
					}
					break;
				case 7: //º¹½Â
					switch(together){
					case 0:
						domerace[k].tx_BB_10_20 = getSS(current);
						break;
					case 1:
						domerace[k].t1_BB_11_12 = getSS(current);
						break;
					case 2: 
						int tmp = search_index(current, ' ');
						domerace[k].t2_BB_10_21 = getSS(current.substring(0, tmp));
						domerace[k].t2_BB_10_22 = getSS(current.substring(tmp+1));
						break;
						
					case 3:
						tmp = search_index(current, ' ');
						domerace[k].t3_BB_10_20 = getSS(current);
						break;
					}
					break;
				case 8: //»ïº¹½Â
					switch(together){
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
						domerace[k].t3_3B_12_31 = getSS(current.substring(0, tmp));
						domerace[k].t3_3B_12_32 = getSS(current.substring(tmp+1));
						break;
					}
					break;
				}
			}
			
			doc = Jsoup.connect("http://www.kcycle.or.kr/contents/information/raceResultPage.do").get();
			eles = doc.select(".playResultList tbody tr td");
			
			for(i=0; i<20; i++) kcycle[i] = new RaceResult();
			for(i=0; i<eles.size(); i++){
				if(i%10 == 0) together = 0;
				int k = i==0? 0 : i/10;
				String current = eles.get(i).text();
				kcycle[k].race_kind = RaceResult.RACE_KIND_CYCLE;
				switch(i%10){
				case 0:
					if (current.contains("±¤¸í")) kcycle[k].race_grd = RaceResult.RACE_GRD_CYCLE_GWANGMYEONG;
					else if (current.contains("Ã¢¿ø")) kcycle[k].race_grd = RaceResult.RACE_GRD_CYCLE_CHANGWON;
					else if (current.contains("ºÎ»ê")) kcycle[k].race_grd = RaceResult.RACE_GRD_CYCLE_PUSAN;
					kcycle[k].race_no = toInt(current); 
					break;
				case 1:
					//1µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						
						kcycle[k].race_1st_1 = circletonum(current.charAt(0)+"");
						kcycle[k].race_1st_2 = circletonum(current.charAt(tmp)+"");
						kcycle[k].race_1stN_1 = current.substring(1, tmp-1);
						kcycle[k].race_1stN_2 = current.substring(tmp+2);
						together = 1;
					}
					//1µîµ¿Âø ¾Æ´Ô.
					else{
						kcycle[k].race_1stN_1 = current.substring(1);
						kcycle[k].race_1st_1 = circletonum(current.charAt(0)+"");
					}
					break;
//////////**********






¿©±â±îÁö Çß½À´Ï´Ù!
~~~~~~~~





























**************/
				case 2:
					if(together == 1) break;
					//2µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						spo1_co_kr[k].race_2nd_1 = current.charAt(0);
						spo1_co_kr[k].race_2nd_2 = current.charAt(tmp);
						spo1_co_kr[k].race_2ndN_1 = current.substring(2, tmp-1);
						spo1_co_kr[k].race_2ndN_2 = current.substring(tmp+2);
						together = 2;
					}
					//2µîµ¿Âø ¾Æ´Ô.
					else{
						spo1_co_kr[k].race_2nd_1 = current.charAt(0);
						spo1_co_kr[k].race_2ndN_1 = current.substring(1);
					}
					break;
				case 3:
					if(together == 2) break;
					//3µî µ¿ÂøÀÎÁö È®ÀÎ
					if(search_num(current, ' ')>=3){
						int tmp = getIndex_int(current, 2);
						spo1_co_kr[k].race_3rd_1 = current.charAt(0);
						spo1_co_kr[k].race_3rd_2 = current.charAt(tmp);
						spo1_co_kr[k].race_3rdN_1 = current.substring(2, tmp-1);
						spo1_co_kr[k].race_3rdN_2 = current.substring(tmp+2);
						together = 3;
					}
					//3µîµ¿Âø ¾Æ´Ô.
					else{
						spo1_co_kr[k].race_3rd_1 = current.charAt(0);
						spo1_co_kr[k].race_3rdN_1 = current.substring(1);
					}
					break;
				case 6: //½Ö½Â
					switch(together){
					case 0:
						spo1_co_kr[k].tx_SS_10_20 = getSS(current);
						break;
					case 1:
						int tmp = search_index(current, ' ');
						spo1_co_kr[k].t1_SS_11_12 = getSS(current.substring(0, tmp));
						spo1_co_kr[k].t1_SS_12_11 = getSS(current.substring(tmp+1));
						break;
					case 2: 
						tmp = search_index(current, ' ');
						spo1_co_kr[k].t2_SS_10_21 = getSS(current.substring(0, tmp));
						spo1_co_kr[k].t2_SS_10_22 = getSS(current.substring(tmp+1));
						break;
						
					case 3:
						tmp = search_index(current, ' ');
						spo1_co_kr[k].t3_SS_10_20 = getSS(current.substring(0, tmp));
						spo1_co_kr[k].t3_SS_10_20 = getSS(current.substring(tmp+1));
						break;
					}
					break;
				case 7: //º¹½Â
					switch(together){
					case 0:
						spo1_co_kr[k].tx_BB_10_20 = getSS(current);
						break;
					case 1:
						spo1_co_kr[k].t1_BB_11_12 = getSS(current);
						break;
					case 2: 
						int tmp = search_index(current, ' ');
						spo1_co_kr[k].t2_BB_10_21 = getSS(current.substring(0, tmp));
						spo1_co_kr[k].t2_BB_10_22 = getSS(current.substring(tmp+1));
						break;
						
					case 3:
						tmp = search_index(current, ' ');
						spo1_co_kr[k].t3_BB_10_20 = getSS(current);
						break;
					}
					break;
				case 8: //»ïº¹½Â
					switch(together){
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
						spo1_co_kr[k].t3_3B_12_31 = getSS(current.substring(0, tmp));
						spo1_co_kr[k].t3_3B_12_32 = getSS(current.substring(tmp+1));
						break;
					}
					break;
				}
			}
		}finally{}
	}
}
