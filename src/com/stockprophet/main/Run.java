package com.stockprophet.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.stockprophet.common.CustomComparators;
import com.stockprophet.common.StockUtil;
import com.stockprophet.common.StockUtil.CalculatedMetricType;
import com.stockprophet.common.StockUtil.PriceType;
import com.stockprophet.math.AdvancedFinancialMathMethods;
import com.stockprophet.math.CommonFinancialMathMethods;
import com.stockprophet.math.CommonLinearAlgebraMethods;
import com.stockprophet.math.GaussianCalculator;
import com.stockprophet.web.Column;
import com.stockprophet.web.GenerateHtml;



public class Run {
	
	public static final int FIVE_YEARS = 1201;
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
		for (String key : indexMap.keySet()) {
			List<Double> prices = StockUtil.getPriceFromFile(key);
			
			if (prices.size() < FIVE_YEARS){
				System.out.println("Not enough data points for " + key + ": " + prices.size());
				continue;
			}
			stocks.put(key, prices);
		}
		timeSteps.add(new Date());
		System.out.println("Time: " + getTime(timeSteps) + " seconds");
		System.out.println("3. Generation of Gaussian Least Squares");
		for(String key : stocks.keySet()){
			
			List<Double> clone = new ArrayList<>();
			for(Double price : stocks.get(key))
				clone.add(price);
			
			Collections.reverse(clone);
			//^ After reversing, index 0 is old, index.size()-1 is new
			
	    	List<List<Double>> coefss = new ArrayList<List<Double>>();
	    	List<List<Double>> yHats = new ArrayList<List<Double>>();
	    	for(int p=3;p<10;p++)
	    		coefss.add(GaussianCalculator.calculateCoefficients(clone, p));
	    	
	    	for(List<Double> coefs : coefss)
    			yHats.add(GaussianCalculator.calculateYHat(clone.size(), coefs));
	    	
	    	File file = new File(key + ".json");
			try{
				file.createNewFile();
				FileWriter writer = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bufferWriter = new BufferedWriter(writer);
				
				BufferedReader bufferReader = new BufferedReader(new FileReader(new File("data/" + key + ".csv")));
				String header = bufferReader.readLine();
				for(int i=0;i<yHats.size();i++)
					header += ",Fit" + (i+3);
				bufferWriter.write(header + "\n");
				String line;
				int lineCounter = 0;
				while((line=bufferReader.readLine())!=null){
					bufferWriter.write(line);
					for(List<Double> yHat : yHats)
						bufferWriter.write("," + 0.0001*Math.round(10000*yHat.get(lineCounter)));
					bufferWriter.write("\n");
					lineCounter++;
				}
				bufferReader.close();
				bufferWriter.close();
			}catch(IOException e){
				
			}
			
	    	
		}
		
		timeSteps.add(new Date());
		System.out.println("Time: " + getTime(timeSteps) + " seconds");
		System.out.println("4. Calculate Metrics");
		//calculate metrics
		for(String key : stocks.keySet()){
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
		
		for(int i=0;i<sortedColumnsYesterday.size();i++)
			yesterdaysRank.put(sortedColumnsYesterday.get(i).get(Column.SYMB), i);
		
		for(int i=0;i<sortedColumnsToday.size();i++)
			if(yesterdaysRank.containsKey(sortedColumnsToday.get(i).get(Column.SYMB)))
				stockRankDiff.put(sortedColumnsToday.get(i).get(Column.SYMB), yesterdaysRank.get(sortedColumnsToday.get(i).get(Column.SYMB))-i);

		System.out.println("YESTERDAY");
		for(int rank=0;rank<20;rank++)
			System.out.println((rank+1) + "\t" + 
					sortedColumnsYesterday.get(rank).get(Column.SYMB) + "\t" +
					sortedColumnsYesterday.get(rank).get(Column.LINEAR) + "\t" +
					sortedColumnsYesterday.get(rank).get(Column.RIGID) + "\t" +
					sortedColumnsYesterday.get(rank).get(Column.TURB) + "\t" +
					sortedColumnsYesterday.get(rank).get(Column.YEAR1)
					);
		
		System.out.println("TODAY");
		for(int rank=0;rank<20;rank++)
			System.out.println((rank+1) + "\t" + 
					sortedColumnsToday.get(rank).get(Column.SYMB) + "\t" +
					sortedColumnsToday.get(rank).get(Column.LINEAR) + "\t" +
					sortedColumnsToday.get(rank).get(Column.RIGID) + "\t" +
					sortedColumnsToday.get(rank).get(Column.TURB) + "\t" +
					sortedColumnsToday.get(rank).get(Column.YEAR1)
					);
		
		for(int rank=0;rank<sortedColumnsToday.size();rank++){
			sortedColumnsToday.get(rank).put(Column.DIFF, "" + stockRankDiff.get(sortedColumnsToday.get(rank).get(Column.SYMB)));
			sortedColumnsToday.get(rank).put(Column.RANK, "" + (rank+1));
		}
		
		timeSteps.add(new Date());
		System.out.println("Time: " + getTime(timeSteps) + " seconds");
		System.out.println("5. Generation Website");

//		GenerateJSON.generateJsonForMobile(sortedColumnsToday, Column.DAY5);
//		GenerateJSON.generateJsonForMobile(sortedColumnsToday, Column.MONTH1);
//		GenerateJSON.generateJsonForMobile(sortedColumnsToday, Column.MONTH6);
//		GenerateJSON.generateJsonForMobile(sortedColumnsToday, Column.YEAR1);
//		GenerateJSON.generateJsonForMobile(sortedColumnsToday, Column.YEAR5);
		GenerateHtml.writeWeb(sortedColumnsToday);
		GenerateHtml.writePlotHtml();
//		GenerateHtml.writeMobile();
		
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
			if(
					column != Column.RANK && 
					column != Column.COMPANY && 
					column != Column.SECTOR && 
					column != Column.INDUSTRY && 
					column != Column.SYMB && 
					column != Column.DIFF && 
					column != Column.PRICE
				){
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
		
		List<Double> prices = allPrices.subList(startingPoint, FIVE_YEARS-1+startingPoint);
		
		
		HashMap<CalculatedMetricType, Double> metricMap= StockUtil.calculateMetrics(prices);

		columns.put(Column.LINEAR, "" + 100*metricMap.get(CalculatedMetricType.ALGO_SCORE));
		columns.put(Column.RIGID, "" + 100*metricMap.get(CalculatedMetricType.RIGIDITY_SCORE));
		columns.put(Column.TURB, "" + 100*metricMap.get(CalculatedMetricType.TURBULANCE_SCORE));
		
		List<Double> open = StockUtil.getPriceFromFile(symbol, PriceType.OPEN);  
		List<Double> high = StockUtil.getPriceFromFile(symbol, PriceType.HIGH);  
		List<Double> low = StockUtil.getPriceFromFile(symbol, PriceType.LOW);  
		List<Double> volume = StockUtil.getPriceFromFile(symbol, PriceType.VOLUME);  
		
		double noise = 200*(high.get(high.size()-1) - low.get(low.size()-1))/((high.get(high.size()-1) + low.get(low.size()-1)));
		columns.put(Column.NOISE, "" + noise);
		double marketCap = Math.log10(volume.get(volume.size()-1) * prices.get(0));
		columns.put(Column.MKTCAP, "" + marketCap);
		
		double[] metrics = AdvancedFinancialMathMethods.calculateOptimalAndMaximalIntradayReturns(open, high);
		
		columns.put(Column.OIDR, "" + metrics[0]);
		columns.put(Column.MIDR, "" + metrics[1]);
		
		columns.put(Column.WEEK1, "" + 100*StockUtil.calculateGrowth(prices.subList(0, 5))); 
		columns.put(Column.MONTH1, "" + 100*StockUtil.calculateGrowth(prices.subList(0, 20))); 
		columns.put(Column.MONTH3, "" + 100*StockUtil.calculateGrowth(prices.subList(0, 60))); 
		columns.put(Column.MONTH6, "" + 100*StockUtil.calculateGrowth(prices.subList(0, 120))); 
		columns.put(Column.YEAR1, "" + 100*StockUtil.calculateGrowth(prices.subList(0, 250)));
		columns.put(Column.YEAR3, "" + 100*StockUtil.calculateGrowth(prices.subList(0, 750)));
		columns.put(Column.YEAR5, "" + 100*StockUtil.calculateGrowth(prices.subList(0, Run.FIVE_YEARS-1)));
		
		columns.put(Column.PRICE, "" + prices.get(0));
		
		List<Double> clone = new ArrayList<>();
		for(Double price : prices)
			clone.add(price);
		
		Collections.reverse(clone);
		//^ After reversing, index 0 is old, index.size()-1 is new
		
    	List<Double> normalized = GaussianCalculator.normalizeDataTo0(clone);
    	List<Double> coefs3 = GaussianCalculator.calculateCoefficients(normalized, 3);
    	List<Double> coefs5 = GaussianCalculator.calculateCoefficients(normalized, 5);

    	List<Double> pricesMDA5 = CommonFinancialMathMethods.calculateMovingAverage(prices);
    	List<Double> pricesMDA5Normalized = GaussianCalculator.normalizeTo0and1(pricesMDA5);
    	double lengthNormalized = Math.sqrt(2.0)/CommonFinancialMathMethods.calculateLength(pricesMDA5Normalized);
    	List<Double> consistencyMetrics = CommonLinearAlgebraMethods.calculateCustomConsistency(prices, 30, 8);
		double consistency1 = Math.exp(-consistencyMetrics.get(0)/100);
		double consistency2 = Math.exp(-consistencyMetrics.get(1)/200);
		double consistency = Math.sqrt(consistency1 * consistency2);
		
		columns.put(Column.CONF, "" + 100*consistencyMetrics.get(3));
		
		columns.put(Column.MOMENT3, "" + 1000*(3*coefs3.get(3)*clone.size()*clone.size() + 2 * coefs3.get(2) * clone.size() + coefs3.get(1)));
		columns.put(Column.INERT3, "" + 1000000*(2*3*coefs3.get(3)*clone.size() + 2*coefs3.get(2)));

		columns.put(Column.MOMENT5, "" + 1000*(
				5*coefs5.get(5)*Math.pow(clone.size(),  4.0) + 
				4*coefs5.get(4)*Math.pow(clone.size(), 3.0) + 
				3*coefs5.get(3)*Math.pow(clone.size(), 2.0) + 
				2*coefs5.get(2)*clone.size() +
				1*coefs5.get(1)
				));
		columns.put(Column.INERT5, "" + 1000000*(
				5*4*coefs5.get(5)*Math.pow(clone.size(), 3.0) + 
				4*3*coefs5.get(4)*Math.pow(clone.size(), 2.0) + 
				3*2*coefs5.get(3)*clone.size() + 
				2*1*coefs5.get(2)
				));
		
		columns.put(Column.STAB, "" + 100*lengthNormalized);
		columns.put(Column.CONS, ""+ 100*consistency);
		
		columns.put(Column.SHARPE, "" + CommonFinancialMathMethods.calculateSharpe(prices));
		columns.put(Column.WILLIAMS, "" + CommonFinancialMathMethods.calculateWilliams(prices));
		
		return columns;
	}
	
	private static String truncate(String nameRaw){
		String name = nameRaw.replaceAll(",? Inc\\.?", "").replaceAll(" [Cc]orp[a-z\\.]+","").replaceAll("\\(page does not exist\\)","");
		return name.length() > 30 ? name.substring(0, 27) + "..." : name;
	}
}