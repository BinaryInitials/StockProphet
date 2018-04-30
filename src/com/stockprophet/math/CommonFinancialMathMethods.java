package com.stockprophet.math;

import java.util.ArrayList;
import java.util.List;

public class CommonFinancialMathMethods {
	
	private static final int WINDOW = 5;
	
	public static List<Double> calculateMovingAverage(List<Double> prices){
		
		List<Double> movingAverage = new ArrayList<Double>();
		for(int i=0;i<=prices.size()-WINDOW;i++){
			double sum = 0;
			for(int j=0;j<WINDOW;j++)
				sum+=prices.get(j+i);
			movingAverage.add(sum/(double)WINDOW);
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
}
