package com.stockprophet.common;

import java.util.Comparator;
import java.util.HashMap;

import com.stockprophet.html.Column;

public class CustomComparators {
	
	public static final Comparator<String> FinalRankComparator = new Comparator<String>() {
		@Override
		public int compare(String e1, String e2){
			Integer sumRank1 = Integer.valueOf(e1.split(",")[1]);
			Integer sumRank2 = Integer.valueOf(e2.split(",")[1]);
			return Integer.compare(sumRank1, sumRank2);
		}
	};
	
	public static final Comparator<HashMap<String, String>> ColumnComparator = new Comparator<HashMap<String, String>>() {
		@Override
		public int compare(HashMap<String, String> e1, HashMap<String, String> e2){
			Double value1 = Double.valueOf(e1.get("VALUE"));
			Double value2 = Double.valueOf(e2.get("VALUE"));
			return -Double.compare(value1, value2);
		}
	};
	
	public static final Comparator<HashMap<Column, String>> StockComparator = new Comparator<HashMap<Column, String>>() {

		@Override
		public int compare(HashMap<Column, String> o1, HashMap<Column, String> o2) {
			Double metric1 = Double.valueOf(o1.get(Column.SCORE));
			Double metric2 = Double.valueOf(o2.get(Column.SCORE));
			return -Double.compare(metric1,metric2);
		}
	};
	
	public static final Comparator<HashMap<String, String>> CorrelComparator = new Comparator<HashMap<String, String>>() {
		
		@Override
		public int compare(HashMap<String, String> r1, HashMap<String, String> r2 ){
			String correlText1 = r1.get("R"); 
			String correlText2 = r2.get("R");
			Double correl1 = Double.valueOf(correlText1);
			Double correl2 = Double.valueOf(correlText2);
			return correl2.compareTo(correl1);
		}
	};
}
