package com.stockprophet.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.stockprophet.common.CustomComparators;
import com.stockprophet.common.Stock;
import com.stockprophet.common.StockUtil;
import com.stockprophet.common.StockUtil.CalculatedMetricType;
import com.stockprophet.html.Column;
import com.stockprophet.html.GeneratePhp;
import com.stockprophet.math.CommonFinancialMathMethods;
import com.stockprophet.math.CommonLinearAlgebraMethods;
import com.stockprophet.math.GaussianCalculator;



public class Run {
	
	public static final int ONE_YEAR = 250;
	public static final int TODAY = 0;
	public static final int YESTERDAY = 1;
	
	public static void main(String[] args) {

		Date tic = new Date();

		System.out.println("1. Loading Index");
		HashMap<String, String> indexMap = GenerateCommonIndexes.generateCommonIndexes();
		
		System.out.println("2. Data Acquisition");
		List<Stock> stocks = new ArrayList<Stock>();
		HashMap<String, Integer> yesterdaysRank = new HashMap<String, Integer>();
		HashMap<String, Integer> stockRankDiff = new HashMap<String, Integer>();
		
		List<HashMap<Column, String>> columnsToday = new ArrayList<HashMap<Column, String>>();
		List<HashMap<Column, String>> columnsYesterday = new ArrayList<HashMap<Column, String>>();
		
		
		//populate historical prices from file
		for (String key : indexMap.keySet()) {
			List<Double> prices = StockUtil.getPriceFromFile(key);
			
			if (prices.size() < ONE_YEAR)
				continue;
			stocks.add(new Stock(key, indexMap.get(key), prices));
		}
		
		
		System.out.println("3. Calculate Metrics");
		//calculate metrics
		for(Stock stock : stocks){
			HashMap<Column, String> columnToday = populateColumns(stock, TODAY);
			if(columnToday != null){
				columnsToday.add(columnToday);
			}

			HashMap<Column, String> columnYesterday = populateColumns(stock, YESTERDAY);
			if(columnYesterday != null){
				columnsYesterday.add(columnYesterday);
			}
		}
		
		//Overall ranking
		HashMap<String, Integer> totalRank = new HashMap<String, Integer>();
		for (HashMap<Column, String> row : columnsToday)
			totalRank.put(row.get(Column.SYMBOL),0);
		for(Column column : Column.values())
			if(column != Column.RANK && column != Column.COMPANY && column != Column.SYMBOL && column != Column.DIFF && column != Column.SCORE && column != Column.MOMENT && column != Column.INERT){
				System.out.println("RANKING " + column.toString());
				List<HashMap<String, String>> entries = new ArrayList<HashMap<String, String>>();
				for(HashMap<Column, String> row : columnsToday){
					HashMap<String, String> entrie = new HashMap<String, String>();
					entrie.put("KEY", row.get(Column.SYMBOL));
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
		
		System.out.println("FINAL RANK:");
		for(int i=0;i<20;i++){
			String key = hackyList.get(i).split(",")[0];
			Integer score = Integer.valueOf(hackyList.get(i).split(",")[1]);
			System.out.println((i+1) + "\t" + key + "\t" + score);
		}
		
		Collections.sort(columnsToday, CustomComparators.StockComparator);
		Collections.sort(columnsYesterday, CustomComparators.StockComparator);
		
		for(int i=0;i<columnsYesterday.size();i++)
			yesterdaysRank.put(columnsYesterday.get(i).get(Column.SYMBOL), i);
		
		for(int i=0;i<columnsToday.size();i++)
			if(yesterdaysRank.containsKey(columnsToday.get(i).get(Column.SYMBOL)))
				stockRankDiff.put(columnsToday.get(i).get(Column.SYMBOL), yesterdaysRank.get(columnsToday.get(i).get(Column.SYMBOL))-i);

		System.out.println("YESTERDAY");
		for(int rank=0;rank<20;rank++)
			System.out.println((rank+1) + "\t" + 
					columnsYesterday.get(rank).get(Column.SYMBOL) + "\t" +
					columnsYesterday.get(rank).get(Column.ALGO) + "\t" +
					columnsYesterday.get(rank).get(Column.RIGID) + "\t" +
					columnsYesterday.get(rank).get(Column.TURBUL) + "\t" +
					columnsYesterday.get(rank).get(Column.YEAR1)
					);
		
		System.out.println("TODAY");
		for(int rank=0;rank<20;rank++)
			System.out.println((rank+1) + "\t" + 
					columnsToday.get(rank).get(Column.SYMBOL) + "\t" +
					columnsToday.get(rank).get(Column.ALGO) + "\t" +
					columnsToday.get(rank).get(Column.RIGID) + "\t" +
					columnsToday.get(rank).get(Column.TURBUL) + "\t" +
					columnsToday.get(rank).get(Column.YEAR1)
					);
		
		for(int rank=0;rank<columnsToday.size();rank++){
			columnsToday.get(rank).put(Column.DIFF, "" + stockRankDiff.get(columnsToday.get(rank).get(Column.SYMBOL)));
			columnsToday.get(rank).put(Column.RANK, "" + (rank+1));
		}
		System.out.println("4. Generation Website");
		GeneratePhp.write(columnsToday);
		
		Date toc = new Date();
		System.out.println(toc.getTime() - tic.getTime() + " msec");
	}
	

	public static HashMap<Column, String> populateColumns(Stock stock, int startingPoint){
		HashMap<Column, String> columns = new HashMap<Column, String>();
		columns.put(Column.SYMBOL, stock.getSymbol());
		columns.put(Column.COMPANY, truncate(stock.getCompany()));

		List<Double> prices = stock.getPrices().subList(startingPoint, ONE_YEAR-1+startingPoint);
		
		HashMap<CalculatedMetricType, Double> metricMap= StockUtil.calculateSeparateMetrics(prices);
		Double score = StockUtil.calculatedMetric(metricMap);
		if(score == null || score.isNaN())
			return null;

		columns.put(Column.SCORE, "" + 100*score);
		columns.put(Column.ALGO, "" + 100*metricMap.get(CalculatedMetricType.ALGO_SCORE));
		columns.put(Column.RIGID, "" + 100*metricMap.get(CalculatedMetricType.RIGIDITY_SCORE));
		columns.put(Column.TURBUL, "" + 100*metricMap.get(CalculatedMetricType.TURBULANCE_SCORE));
		
		columns.put(Column.MONTH1, "" + 100*StockUtil.calculateGrowth(prices.subList(0, 20))); 
		columns.put(Column.MONTH3, "" + 100*StockUtil.calculateGrowth(prices.subList(0, 60))); 
		columns.put(Column.MONTH6, "" + 100*StockUtil.calculateGrowth(prices.subList(0, 120))); 
		
		columns.put(Column.YEAR1, "" + 100*StockUtil.calculateGrowth(prices)); 
		
		List<Double> clone = new ArrayList<>();
		for(Double price : prices)
			clone.add(price);
    	List<Double> subprices = clone.subList(0, 20);
		
		Collections.reverse(subprices);
		//^ After reversing, index 0 is old, index.size()-1 is new
		
    	List<Double> normalized = GaussianCalculator.normalizeDataTo0(subprices);
    	List<Double> coefs = GaussianCalculator.calculateCoefficients(normalized, 3);

    	List<Double> pricesMDA5 = CommonFinancialMathMethods.calculateMovingAverage(prices);
    	List<Double> pricesMDA5Normalized = GaussianCalculator.normalizeTo0and1(pricesMDA5);
    	double lengthNormalized = Math.sqrt(2.0)/CommonFinancialMathMethods.calculateLength(pricesMDA5Normalized);
    	List<Double> consistencyMetrics = CommonLinearAlgebraMethods.calculateCustomConsistency(prices, 30, 8);
		double consistency1 = Math.exp(-consistencyMetrics.get(0)/100);
		double consistency2 = Math.exp(-consistencyMetrics.get(1)/200);
		double consistency = Math.sqrt(consistency1 * consistency2);
		
		columns.put(Column.CONF, "" + 100*consistencyMetrics.get(3));
		columns.put(Column.MOMENT, "" + 100*(3*coefs.get(3)*subprices.size()*subprices.size() + 2 * coefs.get(2) * subprices.size() + coefs.get(1)));
		columns.put(Column.INERT, "" + 1000*(6*coefs.get(3)*subprices.size() + 2 * coefs.get(2)));
		columns.put(Column.STAB, "" + 100*lengthNormalized);
		columns.put(Column.CONSIST, ""+ 100*consistency);
		
		return columns;
	}
	
	private static String truncate(String nameRaw){
		String name = nameRaw.replaceAll(",? Inc\\.?", "").replaceAll("\\(page does not exist\\)","");
		if(name.length() > 20)
			return name.substring(0, 17) + "...";
		return name;
	}
}