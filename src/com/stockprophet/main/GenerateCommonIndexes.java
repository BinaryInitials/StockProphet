package com.stockprophet.main;

import java.util.HashMap;

import com.stockprophet.common.StockUtil;

public class GenerateCommonIndexes {

	public static void main(String[] args) {
		HashMap<String, String[]> map = generateCommonIndexes();
		for(String key : map.keySet())
			System.out.println(key + "\t" + map.get(key)[0] + "\t" + map.get(key)[1] + "\t" + map.get(key)[2]);
	}
	
	public static HashMap<String, String[]> generateCommonIndexes(){
		HashMap<String, String[]> indexMap = new HashMap<String, String[]>();
		indexMap.put("SPY", new String[]{"SPDR S&P 500 ETF Trust", "Index", "Index"});
		indexMap.put("TQQQ", new String[]{"ProShares UltraPro QQQ", "Index", "Index"});
		indexMap.put("SQ", new String[]{"Information Technology", "Internet Software & Services", "Square"});
		indexMap.put("TEAM", new String[]{"Information Technology", "Internet Software & Services", "Atlasian"});
		indexMap.put("MDB", new String[]{"Information Technology", "Internet Software & Services", "Mongo DB"});
		indexMap.put("YEXT", new String[]{"Information Technology", "Internet Software & Services", "Yext"});
		indexMap.put("SFIX", new String[]{"Information Technology", "Internet Software & Services", "Stitch Fix"});
		indexMap.put("SNAP", new String[]{"Information Technology", "Internet Software & Services", "Snapchat"});
		indexMap.put("YELP", new String[]{"Information Technology", "Internet Software & Services", "Yelp"});
		indexMap.putAll(StockUtil.generateSPwithSectorsMap());
		indexMap = StockUtil.cleanMap(indexMap);
		return indexMap;
	}
}