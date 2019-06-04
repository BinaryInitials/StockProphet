package com.stockprophet.math;

import java.util.ArrayList;
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
}
