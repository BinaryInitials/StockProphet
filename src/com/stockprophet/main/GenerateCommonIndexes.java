package com.stockprophet.main;

import java.util.HashMap;

import com.stockprophet.common.StockUtil;
import com.stockprophet.common.StockUtil.IndexType;

public class GenerateCommonIndexes {

	public static void main(String[] args) {
		HashMap<String, String> map = generateCommonIndexes();
		for(String key : map.keySet()){
			System.out.println(key);
		}
	}
	
	public static HashMap<String, String> generateCommonIndexes(){
		HashMap<String, String> indexMap = StockUtil.generateMap(IndexType.DOWJONES);
		indexMap.put("SPY", "SPDR S&P 500 ETF Trust");
		indexMap.put("TQQQ", "ProShares UltraPro QQQ");
		indexMap.put("AEO", "American Eagle Outfiters");
		indexMap.put("AMD", "Advanced Microsystems");
		indexMap.put("BABA", "Alibaba Group Holding");
		indexMap.put("BRK.A", "Berkshire Hathaway");
		indexMap.put("CAKE", "CheeseCake Factory");
		indexMap.put("DDS", "Dillard's");
		indexMap.put("DKS", "Dick's Sporting Goods");
		indexMap.put("DNKN", "Dunkin' Brands");
		indexMap.put("DPZ", "Domino's Pizza");
		indexMap.put("FIT","Fitbit");
		indexMap.put("JACK", "Jack In a Box");
		indexMap.put("JBLU", "Jet Blue Airways");
		indexMap.put("JCP", "JC Penney");
		indexMap.put("NATI", "National Instruments");
		indexMap.put("NUVA", "NuVasive");
		indexMap.put("NYT", "New York Times");
		indexMap.put("PZZA", "Papa John's");
		indexMap.put("TTWO", "Take-Two Interactive");
		indexMap.put("TWLO","Twilio");
		indexMap.put("TWTR","Twitter");
		indexMap.put("URBN", "Urban Outfitters");
		indexMap.put("WBMD", "WebMD Health");
		indexMap.put("WEN", "Wendy's");
		indexMap.put("ZNGA", "Zynga");
		indexMap.putAll(StockUtil.generateMap(IndexType.NASDAQ100));
		indexMap.putAll(StockUtil.generateMap(IndexType.SP500));
		//Very speculative
		//FUCK THE SP400 FUCK THEM!!!!
		indexMap.putAll(StockUtil.generateMap(IndexType.SP400));
		
		indexMap = StockUtil.cleanMap(indexMap);
		return indexMap;
	}
}