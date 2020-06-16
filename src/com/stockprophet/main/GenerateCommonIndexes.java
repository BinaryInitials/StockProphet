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
		indexMap.put("BYND", new String[]{"Consumer Staples", "Packaged Foods & Meats", "Beyond Meat"});
		indexMap.put("SQ", new String[]{"Information Technology", "Internet Software & Services", "Square"});
		indexMap.put("TEAM", new String[]{"Information Technology", "Internet Software & Services", "Atlassian"});
		indexMap.put("LULU", new String[]{"Consumer Discretionary", "Department Stores", "Lululemon"});
		indexMap.put("WEN", new String[]{"Consumer Discretionary", "Restaurants", "Wendys"});
		indexMap.put("BLMN", new String[]{"Consumer Discretionary", "Restaurants", "Bloomin Brands"});
		indexMap.put("DPZ", new String[]{"Consumer Discretionary", "Restaurants", "Dominos"});
		indexMap.put("LYFT", new String[]{"Information Technology", "Internet Software & Services", "LYFT"});
		indexMap.put("UBER", new String[]{"Information Technology", "Internet Software & Services", "UBER"});
		indexMap.put("FIT", new String[]{"Information Technology", "Internet Software & Services", "Fitbit"});
		indexMap.put("SIRI", new String[]{"Information Technology", "Internet Software & Services", "SIRI Radio"});
		indexMap.put("PYPL", new String[]{"Information Technology", "Internet Software & Services", "PayPal"});
		indexMap.put("PINS", new String[]{"Information Technology", "Internet Software & Services", "Pinterest"});
		indexMap.put("MDB", new String[]{"Information Technology", "Internet Software & Services", "Mongo DB"});
		indexMap.put("SNAP", new String[]{"Communication Services", "Interactive Media & Services", "Snapchat"});
		indexMap.put("ESTC", new String[]{"Information Technology", "Internet Software & Services", "Elastic"});
		indexMap.put("SPOT", new String[]{"Information Technology", "Internet Software & Services", "Spotify"});
		indexMap.put("BABA", new String[]{"Information Technology", "Internet Software & Services", "Alibaba"});
		indexMap.put("TWLO", new String[]{"Information Technology", "Internet Software & Services", "Twilio"});
		indexMap.put("SHOP", new String[] {"Information Technology", "Internet Software & Services", "Shopify"});
		indexMap.put("YELP", new String[]{"Information Technology", "Internet Software & Services", "Yelp"});
		indexMap.put("MELI", new String[]{"Information Technology", "Internet Software & Services", "MercadoLibre"});
		indexMap.put("ROKU", new String[]{"Information Technology", "Internet Software & Services", "Roku"});
		indexMap.put("TSLA", new String[]{"Consumer Discretionary", "Automobile Manufacturers", "Tesla"});
		indexMap.put("AYX", new String[]{"Information Technology", "Internet Software & Services", "Alteryx"});
		indexMap.put("PTON", new String[]{"Information Technology", "Internet Software & Services", "Peleton"});
		indexMap.put("WORK", new String[]{"Information Technology", "Internet Software & Services", "Slack"});
		indexMap.put("INSG", new String[]{"Information Technology", "Communications Equipment", "Inseego"});
		indexMap.put("ZM", new String[]{"Communication Services", "Interactive Media & Services", "Zoom Video Communications"});
		
		

		HashMap<String, String[]> indexMapRightOrder = new HashMap<String, String[]>();
		for(String key : indexMap.keySet()) {
			String company = indexMap.get(key)[2];
			String sector = indexMap.get(key)[0];
			String industry = indexMap.get(key)[1];
			indexMapRightOrder.put(key, new String[] {company, sector, industry});
		}
		
		indexMapRightOrder.putAll(StockUtil.generateSPwithSectorsMap());
		indexMapRightOrder = StockUtil.cleanMap(indexMapRightOrder);
		return indexMapRightOrder;
	}
}
