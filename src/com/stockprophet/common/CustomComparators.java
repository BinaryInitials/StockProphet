package com.stockprophet.common;

import java.util.Comparator;
import java.util.HashMap;

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
	
}
