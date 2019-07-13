package com.stockprophet.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.stockprophet.common.CustomComparators;
import com.stockprophet.common.StockUtil;
import com.stockprophet.common.StockUtil.CalculatedMetricType;
import com.stockprophet.common.StockUtil.PriceType;
import com.stockprophet.math.AdvancedFinancialMathMethods;
import com.stockprophet.web.Column;
import com.stockprophet.web.GenerateHtml;

public class Run {
	
	public static final int ONE_YEAR = 251;
	public static final int TODAY = 0;
	public static final int YESTERDAY = 1;
	
	public static void main(String[] args) throws IOException {
		List<Date> timeSteps = new ArrayList<Date>();
		
		timeSteps.add(new Date());
		System.out.println("1. Loading Index");
		
		HashMap<String, String[]> indexMap = GenerateCommonIndexes.generateCommonIndexes();
		
		timeSteps.add(new Date());
		System.out.println("Time: " + getTime(timeSteps) + " seconds");
		System.out.println("2. Data Acquisition");

		HashMap<String, List<Double>> stocks = new HashMap<String, List<Double>>();
		HashMap<String, Integer> yesterdaysRank = new HashMap<String, Integer>();
		HashMap<String, Integer> stockRankDiff = new HashMap<String, Integer>();
		
		List<HashMap<Column, String>> columnsToday = new ArrayList<HashMap<Column, String>>();
		List<HashMap<Column, String>> columnsYesterday = new ArrayList<HashMap<Column, String>>();
		
		//populate historical prices from file
		Set<String> existingKeys = new HashSet<String>();
		for (String key : indexMap.keySet()) {
			List<Double> prices = StockUtil.getPriceFromFile(key);
			
			if (prices.size() < ONE_YEAR)
				continue;
			
			existingKeys.add(key);
			stocks.put(key, prices);
		}
		
		System.out.println("EXISTING KEYS: " + existingKeys);
		
		timeSteps.add(new Date());
		System.out.println("Time: " + getTime(timeSteps) + " seconds");
		System.out.println("3. Calculate Metrics");

		//calculate metrics
		for(String key : existingKeys){
			HashMap<Column, String> columnToday = populateColumns(key, indexMap.get(key), stocks.get(key), TODAY);
			if(columnToday != null){
				columnsToday.add(columnToday);
			}else{
				System.out.println("Error found for " + key);
			}

			HashMap<Column, String> columnYesterday = populateColumns(key, indexMap.get(key), stocks.get(key), YESTERDAY);
			if(columnYesterday != null){
				columnsYesterday.add(columnYesterday);
			}
		}
		
		List<HashMap<Column, String>> sortedColumnsToday = overallRanking(columnsToday);
		List<HashMap<Column, String>> sortedColumnsYesterday = overallRanking(columnsYesterday);
		
		timeSteps.add(new Date());
		System.out.println("Time: " + getTime(timeSteps) + " seconds");
		System.out.println("4. Generation Website");

		for(int i=0;i<sortedColumnsYesterday.size();i++)
			yesterdaysRank.put(sortedColumnsYesterday.get(i).get(Column.SYMB), i);
		
		for(int i=0;i<sortedColumnsToday.size();i++)
			if(yesterdaysRank.containsKey(sortedColumnsToday.get(i).get(Column.SYMB)))
				stockRankDiff.put(sortedColumnsToday.get(i).get(Column.SYMB), yesterdaysRank.get(sortedColumnsToday.get(i).get(Column.SYMB))-i);

		System.out.println("YESTERDAY");
		for(int rank=0;rank<Math.min(20, sortedColumnsYesterday.size());rank++)
			System.out.println((rank+1) + "\t" + 
					sortedColumnsYesterday.get(rank).get(Column.SYMB) + "\t" +
					sortedColumnsYesterday.get(rank).get(Column.LINEAR) + "\t" +
					sortedColumnsYesterday.get(rank).get(Column.RIGID)
					);
		
		System.out.println("TODAY");
		for(int rank=0;rank<Math.min(20, sortedColumnsToday.size());rank++)
			System.out.println((rank+1) + "\t" + 
					sortedColumnsToday.get(rank).get(Column.SYMB) + "\t" +
					sortedColumnsToday.get(rank).get(Column.LINEAR) + "\t" +
					sortedColumnsToday.get(rank).get(Column.RIGID)
					);
		
		for(int rank=0;rank<sortedColumnsToday.size();rank++){
			sortedColumnsToday.get(rank).put(Column.DIFF, "" + stockRankDiff.get(sortedColumnsToday.get(rank).get(Column.SYMB)));
			sortedColumnsToday.get(rank).put(Column.RANK, "" + (rank+1));
		}
		
		GenerateHtml.writeWeb(sortedColumnsToday);
		GenerateHtml.writePlotHtml();
		
		timeSteps.add(new Date());
		System.out.println("Total Elapsed time: "  + getTime(timeSteps, true)/60 + " minutes");
	}
	
	public static long getTime(List<Date> steps){
		return getTime(steps, false);
	}
	
	public static long getTime(List<Date> steps, boolean total){
		return (steps.get(steps.size()-1).getTime()-steps.get(total ? 0 : steps.size()-2).getTime())/1000;
	}
	
	public static List<HashMap<Column, String>> overallRanking(List<HashMap<Column, String>> columns){
		HashMap<String, Integer> totalRank = new HashMap<String, Integer>();
		for (HashMap<Column, String> row : columns)
			totalRank.put(row.get(Column.SYMB),0);
		for(Column column : Column.values())
			if(column.isRanking()){
				System.out.println("RANKING " + column.toString());
				List<HashMap<String, String>> entries = new ArrayList<HashMap<String, String>>();
				for(HashMap<Column, String> row : columns){
					HashMap<String, String> entrie = new HashMap<String, String>();
					entrie.put("KEY", row.get(Column.SYMB));
					entrie.put("VALUE", row.get(column));
					entries.add(entrie);
				}
				Collections.sort(entries, CustomComparators.ColumnComparator);
				for(int i=0;i<entries.size();i++)
					totalRank.put(entries.get(i).get("KEY"), totalRank.get(entries.get(i).get("KEY")) + i);
			}

		List<String> hackyList = new ArrayList<String>();
		for(String key : totalRank.keySet())
			hackyList.add(key + "," + totalRank.get(key));
		Collections.sort(hackyList, CustomComparators.FinalRankComparator);
		List<HashMap<Column, String>> sortedColumns = new ArrayList<HashMap<Column, String>>();
		for(String item : hackyList){
			String symbol = item.split(",")[0];
			for(HashMap<Column, String> column : columns){
				if(column.get(Column.SYMB).equals(symbol)){
					sortedColumns.add(column);
					break;
				}
			}
		}
		return sortedColumns;
	}
	
	
	public static HashMap<Column, String> populateColumns(String symbol, String[] properties, List<Double> allPrices, int startingPoint){
		HashMap<Column, String> columns = new HashMap<Column, String>();
		columns.put(Column.SYMB, symbol);
		columns.put(Column.COMPANY, truncate(properties[0]));
		columns.put(Column.SECTOR, properties[1]);
		columns.put(Column.INDUSTRY, properties[2]);
		
		List<Double> prices = allPrices.subList(startingPoint, ONE_YEAR-1+startingPoint);
		HashMap<CalculatedMetricType, Double> metricMap= StockUtil.calculateMetrics(prices);

		columns.put(Column.LINEAR, "" + 100*metricMap.get(CalculatedMetricType.ALGO_SCORE));
		columns.put(Column.RIGID, "" + 100*metricMap.get(CalculatedMetricType.RIGIDITY_SCORE));
		columns.put(Column.GROWTH, "" + 100*metricMap.get(CalculatedMetricType.GROWTH_SCORE));
		
		List<Double> open = StockUtil.getPriceFromFile(symbol, PriceType.OPEN);  
		List<Double> high = StockUtil.getPriceFromFile(symbol, PriceType.HIGH);  
		List<Double> volume = StockUtil.getPriceFromFile(symbol, PriceType.VOLUME);  
		
		double marketCap = Math.log10(volume.get(0) * prices.get(0));
		columns.put(Column.MKTCAP, "" + 100*(1 - Math.exp(-(marketCap-6.0)*Math.log(100)/(5))));
		
		double[] metrics = AdvancedFinancialMathMethods.calculateOptimalAndMaximalIntradayReturns(open, high);
		
		columns.put(Column.OIDR, "" + metrics[0]);
		columns.put(Column.MIDR, "" + metrics[1]);
		columns.put(Column.PRICE, "" + prices.get(0));
		
		double sum = 0.0;
		double sum2 = 0.0;
		for(int i=0;i<5;i++) {
			sum += prices.get(i);
			sum2 += prices.get(i)*prices.get(i);
		}
		double mva5 = sum/5.0;
		for(int i=5;i<13;i++) {
			sum += prices.get(i);
		}
		double mva13 = sum/13.0;
		columns.put(Column.MVA, "" + (mva5-mva13));
		
		double stdev = Math.sqrt(sum2/5.0 - mva5*mva5);
		columns.put(Column.BBAND, "" + (prices.get(0) - (mva5 - 2*stdev)) / (4*stdev));
		return columns;
	}
	
	private static String truncate(String nameRaw){
		String name = nameRaw.replaceAll(",? Inc\\.?", "").replaceAll(" [Cc]orp[a-z\\.]+","").replaceAll("\\(page does not exist\\)","");
		return name.length() > 30 ? name.substring(0, 27) + "..." : name;
	}
}