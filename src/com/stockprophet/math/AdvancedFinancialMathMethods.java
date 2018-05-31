package com.stockprophet.math;

import java.util.ArrayList;
import java.util.List;

public class AdvancedFinancialMathMethods {
	
	
	public static double[] calculateOptimalAndMaximalIntradayReturns(List<Double> open, List<Double> high){
		List<Double> highOpenReturns = new ArrayList<Double>();
		for(int i=0;i<open.size();i++){
			highOpenReturns.add(high.get(i)/open.get(i));
		}
		List<Double> categories = new ArrayList<Double>();
		double delta = 0.000125;
		double category = 1.0-delta;
		while(category < 1.032+delta)
			categories.add(category+=delta);
		List<Integer> counts = new ArrayList<Integer>();
		for(int i=0;i<categories.size();i++)
			counts.add(0);
		
		for(double highOpenReturn : highOpenReturns)
			for(int i=0;i<categories.size();i++)
				if(highOpenReturn > categories.get(i))
					counts.set(i, counts.get(i)+1);

		List<Double> frequencies = new ArrayList<Double>(); 
		for(int count : counts)
			frequencies.add(count/(double)highOpenReturns.size());
		double optYearlyReturn = 0.0;
		double optIntraDayReturn = 0.0;
		double maxIntraDayReturn = 0.0;
		
		for(int i=0;i<categories.size();i++){
			double yearlyReturn = Math.pow(categories.get(i), 250*frequencies.get(i)) * Math.pow(2-categories.get(i), 250*(1-frequencies.get(i)));
			if(yearlyReturn > optYearlyReturn){
				optYearlyReturn = yearlyReturn;
				optIntraDayReturn = categories.get(i);
			}
			if(yearlyReturn < 1.0){
				maxIntraDayReturn = categories.get(i);
				break;
			}
		}
		
		return new double[]{100*optIntraDayReturn-100, 100*maxIntraDayReturn-100};
	}

}
