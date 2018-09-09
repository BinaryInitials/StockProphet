package com.stockprophet.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommonFinancialMathMethods {
	
	private static final int DEFAULT_WINDOW = 5;
	
	public static List<Double> calculateMovingAverage(List<Double> prices){
		return calculateMovingAverage(prices, DEFAULT_WINDOW);
	}
	public static List<Double> calculateMovingAverage(List<Double> prices, int window){
		
		List<Double> movingAverage = new ArrayList<Double>();
		for(int i=0;i<=prices.size()-window;i++){
			double sum = 0;
			for(int j=0;j<window;j++)
				sum+=prices.get(j+i);
			movingAverage.add(sum/(double)window);
		}
		return movingAverage;
	}

	public static double calculateLength(List<Double> prices){
		double sum = 0;
		double deltaX = 1./(prices.size() - 1.0);
		for(int i=1;i<prices.size();i++)
			sum+=Math.sqrt((prices.get(i)-prices.get(i-1))*(prices.get(i)-prices.get(i-1)) + deltaX*deltaX);
		return sum;
	}
	
	public static double calculateWilliams(List<Double> prices){
		return -100.0*(Collections.max(prices.subList(0, 13))-prices.get(0))/(Collections.max(prices.subList(0, 13))-Collections.min(prices.subList(0, 13)));
	}
	
	public static double calculateSharpe(List<Double> prices){
		List<Double> returns = new ArrayList<Double>();
		for(int i=0;i<prices.size()-1;i++)
			returns.add((prices.get(i)-prices.get(i+1))/prices.get(i+1));
		return 100*StatsClass.getAverage(returns)/StatsClass.getStandardDeviation(returns);
	}
}
