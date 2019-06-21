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
import com.stockprophet.math.GaussianCalculator;
import com.stockprophet.web.Column;
import com.stockprophet.web.GenerateHtml;

public class Run {
	
	public static final int ONE_YEAR = 251;
	public static final int TODAY = 0;
	public static final int YESTERDAY = 1;
	public static final int MVA_KERNEL = 5; 
	
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
			
			if (prices.size() < ONE_YEAR){
				System.out.println("Not enough data points for " + key + ": " + prices.size());
				continue;
			}
			stocks.put(key, prices);
		}
		timeSteps.add(new Date());
		System.out.println("Time: " + getTime(timeSteps) + " seconds");
		System.out.println("3. Generation of Gaussian Least Squares and Bollinger Bands");
		HashMap<String, Double> hbands = new HashMap<String, Double>();
		HashMap<String, Double> lbands = new HashMap<String, Double>();
		for(String key : stocks.keySet()){
	    	List<List<Double>> coefss = new ArrayList<List<Double>>();
	    	List<List<Double>> yHats = new ArrayList<List<Double>>();
	    	
	    	List<Double> prices = stocks.get(key).subList(0, ONE_YEAR);
	    	
			List<Double> mavg5 = new ArrayList<Double>();
			List<Double> noise5 = new ArrayList<Double>();
			
			//Calculating Bollinger bands
			for(int i=0;i<prices.size()-MVA_KERNEL;i++) {
				double sum = 0.0;
				double sum2 = 0.0;
				for(int j=0;j<MVA_KERNEL;j++) {
					sum+=prices.get(j+i);
					sum2+=prices.get(j+i)*prices.get(j+i);
				}
				double avg = sum/MVA_KERNEL; 
				mavg5.add(avg);
				noise5.add(Math.sqrt(MVA_KERNEL/(MVA_KERNEL-1)*(sum2/MVA_KERNEL - avg*avg)));
			}
			hbands.put(key, (mavg5.get(0) + noise5.get(0)));
			lbands.put(key, (mavg5.get(0) - noise5.get(0)));
	    	
	    	for(int n=0;n<10;n++)
	    		coefss.add(GaussianCalculator.calculateCoefficients(stocks.get(key).subList(n, ONE_YEAR-10+n), 3));
	    	
	    	for(int i=0;i<coefss.size();i++)
    			yHats.add(GaussianCalculator.calculateYHat(-ONE_YEAR+i, i, coefss.get(i)));
	    	
	    	File file = new File(key + ".json");
			try{
				file.createNewFile();
				FileWriter writer = new FileWriter(file.getAbsoluteFile());
				BufferedWriter bufferWriter = new BufferedWriter(writer);
				
				BufferedReader bufferReader = new BufferedReader(new FileReader(new File("data/" + key.replaceAll("\\.","-") + ".csv")));
				String header = bufferReader.readLine();
				for(int i=0;i<yHats.size();i++)
					header += ",Fit" + i;
				header += ",HBand,LBand";
				
				bufferWriter.write(header + "\n");
				String line;
				List<String> lines = new ArrayList<String>();
				while((line=bufferReader.readLine()) != null)
					if(!line.contains("null") && line.contains(","))
						lines.add(line);
				bufferReader.close();
				for(int i=0;i<ONE_YEAR-MVA_KERNEL;i++){
					String newline = lines.get(lines.size()-ONE_YEAR+i);
					bufferWriter.write(newline);
					for(List<Double> yHat : yHats)
						bufferWriter.write("," + String.format("%.3f", 0.0001*Math.round(10000*yHat.get(i))));
					double hband = mavg5.get(i) + noise5.get(i);
					double lband = mavg5.get(i) - noise5.get(i);
					bufferWriter.write("," + String.format("%.3f", 0.0001*Math.round(10000*hband)) + "," + String.format("%.3f",0.0001*Math.round(10000*lband)));
					bufferWriter.write("\n");
				}
				bufferWriter.close();
			}catch(IOException e){
			}			
	    	
		}
		
		timeSteps.add(new Date());
		System.out.println("Time: " + getTime(timeSteps) + " seconds");
		System.out.println("4. Calculate Metrics");
		//calculate metrics
		for(String key : stocks.keySet()){
			HashMap<Column, String> columnToday = populateColumns(key, indexMap.get(key), stocks.get(key), hbands.get(key), lbands.get(key), TODAY);
			if(columnToday != null){
				columnsToday.add(columnToday);
			}else{
				System.out.println("Error found for " + key);
			}

			HashMap<Column, String> columnYesterday = populateColumns(key, indexMap.get(key), stocks.get(key), hbands.get(key), lbands.get(key), YESTERDAY);
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
					sortedColumnsYesterday.get(rank).get(Column.RIGID)
					);
		
		System.out.println("TODAY");
		for(int rank=0;rank<20;rank++)
			System.out.println((rank+1) + "\t" + 
					sortedColumnsToday.get(rank).get(Column.SYMB) + "\t" +
					sortedColumnsToday.get(rank).get(Column.LINEAR) + "\t" +
					sortedColumnsToday.get(rank).get(Column.RIGID)
					);
		
		for(int rank=0;rank<sortedColumnsToday.size();rank++){
			sortedColumnsToday.get(rank).put(Column.DIFF, "" + stockRankDiff.get(sortedColumnsToday.get(rank).get(Column.SYMB)));
			sortedColumnsToday.get(rank).put(Column.RANK, "" + (rank+1));
		}
		
		timeSteps.add(new Date());
		System.out.println("Time: " + getTime(timeSteps) + " seconds");
		System.out.println("5. Generation Website");

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
	
	
	public static HashMap<Column, String> populateColumns(String symbol, String[] properties, List<Double> allPrices, double hband, double lband, int startingPoint){
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
		
		columns.put(Column.HBAND, "" + hband);
		columns.put(Column.LBAND, "" + lband);
		
		double rating = (prices.get(0) - lband)/(hband-lband)*2-1; 
		
		columns.put(Column.RATING, "" + rating);
		
		return columns;
	}
	
	private static String truncate(String nameRaw){
		String name = nameRaw.replaceAll(",? Inc\\.?", "").replaceAll(" [Cc]orp[a-z\\.]+","").replaceAll("\\(page does not exist\\)","");
		return name.length() > 30 ? name.substring(0, 27) + "..." : name;
	}
}