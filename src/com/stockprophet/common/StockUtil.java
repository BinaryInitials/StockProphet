package com.stockprophet.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.stockprophet.indexes.GenerationNASDAQ;
import com.stockprophet.indexes.GenerationNYSE;
import com.stockprophet.math.StatsClass;

public class StockUtil {
	

	public enum IndexType {
		ALL,
		NASDAQ,
		NYSE,
		SP400,
		SP500,
		NASDAQ100,
		DOWJONES;

		public static String getUrl(IndexType indexType) {
			switch(indexType){
			case ALL:
			case NASDAQ:
				return GenerationNASDAQ.URL_PRIMER;
			case NYSE:
				return GenerationNYSE.URL_PRIMER;
			case SP400:
				return "https://en.wikipedia.org/wiki/List_of_S%26P_400_companies";
			case SP500:
				return "https://en.wikipedia.org/wiki/List_of_S%26P_500_companies";
			case NASDAQ100:
				return "https://en.wikipedia.org/wiki/NASDAQ-100";
			case DOWJONES:
				return "https://en.wikipedia.org/wiki/Dow_Jones_Industrial_Average";
			}
			return null;
		}

		public static String getFilter(IndexType indexType) {
			switch(indexType){
			case SP400:
			case SP500:
				return ".*(www.nyse.com|www.nasdaq.com).*>[A-Z]+<.*";
			case NASDAQ100:
				return "^<li><a.*\\([^a-z]+\\).*";
			case DOWJONES:
				return "^<td><a rel=.nofollow. class=.external text. href=.http.*";
			case ALL:
			case NYSE:
			case NASDAQ:
				break;
			default:
				break;
			}
			return null;
		}
	};
	
	public static double calculateGrowth(List<Double> prices) {
		return prices.get(0)/prices.get(prices.size()-1) - 1;
	}

	public enum CalculatedMetricType {
		ALGO_SCORE,
		GROWTH_SCORE,
		RIGIDITY_SCORE,
	}
	
	public static HashMap<CalculatedMetricType, Double> calculateMetrics(List<Double> prices){
		HashMap<CalculatedMetricType, Double> calculatedMetrics = new HashMap<CalculatedMetricType, Double>();
		double metric1 = StockUtil.calculateScore(prices, PeriodType.ONE_YEAR);
		double rate = (prices.get(0) - prices.get(prices.size() - 1)) / (prices.get(prices.size() - 1));
		double metric2 = 1.0 / (1.0 + Math.exp(-8.0 * rate));
		double metric3 = -StatsClass.getR(prices)*0.5+0.5;
		calculatedMetrics.put(CalculatedMetricType.ALGO_SCORE, metric1);
		calculatedMetrics.put(CalculatedMetricType.GROWTH_SCORE, metric2);
		calculatedMetrics.put(CalculatedMetricType.RIGIDITY_SCORE, metric3);
		return calculatedMetrics;
	}
	
//	public static double calculatedMetric(HashMap<CalculatedMetricType, Double> metrics){
//		return metrics.get(CalculatedMetricType.ALGO_SCORE)*0.50 + 
//			   metrics.get(CalculatedMetricType.GROWTH_SCORE)*0.25 +
//			   metrics.get(CalculatedMetricType.RIGIDITY_SCORE)*0.10 +
//			   metrics.get(CalculatedMetricType.TURBULANCE_SCORE)*0.15;
//	}
	
//	public static double calculateMetric(List<Double> prices){
//		return calculatedMetric(calculateSeparateMetrics(prices));
//	}
	
	public static enum PeriodType {
		FIVE_YEARS,
		ONE_YEAR,
		SIX_MONTHS,
		THREE_MONTHS,
		ONE_MONTH;

		public static int[] getConstants(PeriodType periodType) {
			switch(periodType){
			case FIVE_YEARS:
				return Constants.DAYS_LONG_TERM;
			case ONE_YEAR:
				return Constants.DAYS_SHORT_TERM;
			case SIX_MONTHS:
				return Constants.DAYS_VERY_SHORT_TERM;
			case THREE_MONTHS:
				return Constants.DAYS_VERY_VERY_SHORT_TERM;
			case ONE_MONTH:
				return Constants.DAYS_EXTREMELY_SHORT_TERM;
			default:
				return Constants.DAYS_LONG_TERM;
			}
		}

		public static int getIntervals(PeriodType periodType) {
			switch(periodType){
			case FIVE_YEARS:
				return Constants.FIVE_YEAR_INTERVAL;
			case ONE_YEAR:
				return Constants.ONE_YEAR_INTERVAL;
			case SIX_MONTHS:
				return Constants.SIX_MONTHS_INTERVAL;
			case THREE_MONTHS:
				return Constants.THREE_MONTH_INTERVAL;
			case ONE_MONTH:
				return Constants.ONE_MONTH_INTERVAL;
			default:
				return Constants.FIVE_YEAR_INTERVAL;
			}
		}
	};
	private static final int NUM_OF_YEARS = 10;
	public static final int NUM_OF_DAYS = 250 * NUM_OF_YEARS;
	
	public static double calculateScore(List<Double> list, PeriodType periodType){
		double sum1 = 0.0, sum2 = 0.0;
		int[] constants = PeriodType.getConstants(periodType);
		if(list == null){
			return -1;
		}
		for(int i=findAppropriateStart(list.size(), periodType);i<constants.length;i++){
			Double cOCG = calculateOnCourseGrowth(list, constants[i]);
			Double cLC = calculateLinearCoef(list, constants[i]);
			if(cOCG != null && cLC != null){
				sum1 = sum1 + cOCG*constants[i];
				sum2 = sum2 + cLC*constants[i];	
			}
		}

		return ((sum1+sum2)/(2.0*StatsClass.sumData(constants)) + 1.0)/2.0;
	}
	
	private static Double calculateOnCourseGrowth(List<Double> adjP,int days) {
		List<Double> data = adjP.subList(0, days);
		if(data != null){
			return (data.get(0) - data.get(data.size()-1))/(Collections.max(data) - Collections.min(data));
		}else {
			return null;
		}
	}
	
	private static Double calculateLinearCoef(List<Double> adjP, int days) {
		Double rCoef = StatsClass.getR(adjP.subList(0, days));
		if(rCoef != null)
			return -rCoef;
		return null;
	}
	
	public static Double calculateMovingR(List<Double> adjPAll, PeriodType periodType) {
		int day = PeriodType.getConstants(periodType)[0];
		List<Double> adjPTruncated = adjPAll.subList(0, Math.min(day, adjPAll.size()));
		Double movingR = 0.0;
		int counter = 0;
		for(int i=0;i<adjPTruncated.size()-20;i++){
			counter++;
			Double r = StatsClass.getR(adjPTruncated.subList(i, 20+i));
			if(r == null){
				return null;
			}
			movingR = movingR - r;	
		}
		movingR = movingR /(0.0+counter);
		return movingR;
	}
	
	private static int findAppropriateStart(int laTaille, PeriodType periodType) {
		for(int i=0;i<PeriodType.getConstants(periodType).length;i++){
			if(laTaille > PeriodType.getConstants(periodType)[i]){
				return i;
			}
		}
		return PeriodType.getConstants(periodType).length+1;
	}
	
	public static HashMap<String, String[]> cleanMap(HashMap<String, String[]> map){
		HashMap<String, String[]> cleanMap = new HashMap<String, String[]>();
		for(String key : map.keySet())
			cleanMap.put(key, 
					new String[]{
						map.get(key)[0].
								replaceAll("\\(.*\\)", "").
								replaceAll("&#39;", "'").
								replaceAll("&amp;","&").
								replaceAll(".(Inc|Corp)(\\.|$)","").
								replaceAll(" Corporation","").
								replaceAll(",$",""), 
						map.get(key)[1], 
						map.get(key)[2].replaceAll("&amp;", "&")
								});
		return cleanMap;
	}
	
	public static HashMap<String, String[]> generateSPwithSectorsMap() {
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		URL url;
		List<IndexType> indices = new ArrayList<IndexType>();
		indices.add(IndexType.SP400);
		indices.add(IndexType.SP500);
		for(IndexType indexType : indices){
			try {
				url = new URL(IndexType.getUrl(indexType));
				BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
				String newLine = "";
				List<String> lines = new ArrayList<String>();
				while((newLine = bf.readLine()) != null){
					if(newLine.toLowerCase().contains("sec_filing")){
						while(!(newLine = bf.readLine()).contains("<tr>")){}
						break;
					}
				}
				while((newLine = bf.readLine()) != null){
					if(!newLine.contains("</table>")){
						String clean = newLine.replaceAll("<[^>]+>", "");
						if(!clean.isEmpty())
							lines.add(clean);
					}
				}
				bf.close();
				for(int i=0; i<lines.size();i++){
					String key = "";
					String company = "";
					String sector = "";
					String industry = "";
					if(indexType == IndexType.SP400){
						if(lines.get(i).equals("view")){
							company = lines.get(i-4);
							key = lines.get(i-3);
							sector = lines.get(i-2);
							industry = lines.get(i-1);
							map.put(key, new String[]{company, sector, industry});
						}else if(lines.get(i).endsWith("</table>")){
							break;
						}
					}else if(indexType == IndexType.SP500){
						if(lines.get(i).equals("reports")){
							key = lines.get(i-2);
							company = lines.get(i-1);
							sector = lines.get(i+1);
							industry = lines.get(i+2);
							map.put(key, new String[]{company, sector, industry});
						}else if(lines.get(i).endsWith("</table>")){
							break;
						}
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		return map;
	}
	
	public static HashMap<String, String> generateMap(IndexType indexType) {
		if(indexType == IndexType.NYSE){
			return GenerationNYSE.start(); 
		}else if(indexType == IndexType.NASDAQ){
			return GenerationNASDAQ.getNasdaq();
		}else if(indexType == IndexType.ALL){
			HashMap<String, String> all = GenerationNASDAQ.getNasdaq(); 
			HashMap<String, String> nyse = GenerationNYSE.start();
			for(String key : nyse.keySet()){
				if(all.get(key) == null){
					all.put(key, nyse.get(key));
				}
			}
			return all;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		URL url;
		try {
			url = new URL(IndexType.getUrl(indexType));
			BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
			String newLine = "";
			List<String> lines = new ArrayList<String>();
			while((newLine = bf.readLine()) != null)
				lines.add(newLine.replaceAll("&amp;", "&"));
			for(int i=0; i<lines.size();i++){
				if(indexType == IndexType.NASDAQ100){
					if(lines.get(i).matches(IndexType.getFilter(indexType))){
						String[] parts = lines.get(i).replaceAll(".*>(.*)<.a>.*\\(([^a-z]*)\\).*", "$1" + Constants.SEPARATOR + "$2").split("\\|");
						map.put(parts[1], parts[0].replaceAll("&amp;", "&"));
					}else if(lines.get(i).equals("</ol>")){
						break;
					}
				}else if(indexType == IndexType.SP500 || indexType == IndexType.SP400){
					String key = "";
					String value = "";
					if(lines.get(i).matches(IndexType.getFilter(indexType)) && i < lines.size()-1){
						key = lines.get(i).replaceAll(".*>([A-Z]+)<.*", "$1");
						if(lines.get(i+1).matches(".*title.*")){
							value = lines.get(i+1).replaceAll("<td><a href=\".*\" title=\"", "").replaceAll("\">.*","").replaceAll("\".*", "").replaceAll("&amp;", "&");
						}else if(lines.get(i+2).matches(".*title.*")){
							value = lines.get(i+2).replaceAll("<td><a href=\".*\" title=\"", "").replaceAll("\">.*","").replaceAll("\".*", "").replaceAll("&amp;", "&");
						}
						map.put(key, value);
					}else if(lines.get(i).endsWith("/table>")){
						break;
					}
				}else if(indexType == IndexType.DOWJONES){
					if(lines.get(i).matches(IndexType.getFilter(indexType))){
						String key = lines.get(i).replaceAll("<td><a [a-z]*=\"","").replaceAll(".*title=.","").replaceAll(".*\">", "").replaceAll("<.*","");
						String value = lines.get(i-2).replaceAll(".*>([^\"<>]+)<.a><.td>","$1").replaceAll("&amp;","&");
						map.put(key, value);	
					}
				}
			}
			return map;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	public enum PriceType {
		OPEN(1),
		HIGH(2),
		LOW(3),
		CLOSE(4),
		ADJ(5),
		VOLUME(6);
		
		int index;
		
		PriceType(int index){
			this.index = index; 
		}
		public int getIndex(){
			return index;
		}
			
	}
	
	public static List<Double> getPriceFromFile(String key) {
		return getPriceFromFile(key, PriceType.ADJ);
	}
	public static List<Double> getPriceFromFile(String key, PriceType priceType) {
		List<Double> prices = new ArrayList<Double>();
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(new File("data/" + key.replaceAll("\\.", "-") + ".csv")));
			//Skip headers
			String line = buffer.readLine();
			if(line == null || line.startsWith("{")){
				System.out.println("Skipping " + key);
				buffer.close();
				return prices;
			}
			while((line=buffer.readLine())!=null)
				if(!line.contains("null") && line.contains(","))
					prices.add(Double.valueOf(line.split(",")[priceType.getIndex()]));
			Collections.reverse(prices);
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (NumberFormatException e){
			e.printStackTrace();
		}
		return prices;
	}

}