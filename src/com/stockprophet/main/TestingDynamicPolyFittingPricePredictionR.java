package com.stockprophet.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stockprophet.common.StockUtil;
import com.stockprophet.math.GaussianCalculator;
import com.stockprophet.math.StatsClass;

public class TestingDynamicPolyFittingPricePredictionR {

	public static void main(String[] args) {
		String key = "EHC";
		List<Double> prices = StockUtil.getPriceFromFile(key);
		double maxR2 = 0.0;
		int optimizedN = 0;
		for(int i=20;i<prices.size()-10;i++){
			List<Double> coefs = GaussianCalculator.calculateCoefficients(prices.subList(0, i), 3);
			double r2 = GaussianCalculator.calculateR2(prices.subList(0, i), coefs);
			if(r2 > maxR2){
				maxR2 = r2;
				optimizedN = i;
			}
		}
		{
			List<Double> estimatedPrice = new ArrayList<Double>();
		
		for(int i=0;i<10;i++){
			List<Double> subPrices = prices.subList(i, i+optimizedN);
			List<Double> coefs = GaussianCalculator.calculateCoefficients(subPrices, 3);
			estimatedPrice.add(GaussianCalculator.calculateYHatAtT(i, coefs));
		}
		List<Double> coefsEstimatedPrice = GaussianCalculator.calculateCoefficients(estimatedPrice, 2);
		
		double residuals = GaussianCalculator.calculateResiduals(estimatedPrice, coefsEstimatedPrice);
		System.out.println("Residuals: " + residuals);
		double SST = GaussianCalculator.calculateSST(estimatedPrice);
		System.out.println("SST: " + SST);
		double r2 = GaussianCalculator.calculateR2(estimatedPrice, coefsEstimatedPrice);
		System.out.println(key + "\t" + optimizedN + "\t" + maxR2 + "\t" + coefsEstimatedPrice.get(0) + "\t" + r2);
		}
		//Creating an outlier with 1 off at index = 0;
		{
			List<Double> estimatedPrice = Arrays.asList(232.6209911,226.873315,228.6489816,230.2557708,230.4709469,229.7028244,228.7478707,228.668009,229.4789188);
			System.out.println(estimatedPrice);
			List<Double> beforecoefsEstimatedPrice = GaussianCalculator.calculateCoefficients(estimatedPrice, 2);
			double beforer2 = GaussianCalculator.calculateR2(estimatedPrice, beforecoefsEstimatedPrice);
			System.out.println("BEFORE R2=" + beforer2);
		//Outlier detection;
		int indexOfMax2ndDifference = -1;
		double max2ndDifference = -1;
		for(int i=0;i<estimatedPrice.size()-2;i++){
			double secondDiff = Math.pow(estimatedPrice.get(i) - 2*estimatedPrice.get(i+1) + estimatedPrice.get(i+2), 2.0);
			if(secondDiff > max2ndDifference){
				max2ndDifference = secondDiff;
				indexOfMax2ndDifference = i;
			}
		}
		List<Double> outlierCandidates = Arrays.asList(
				estimatedPrice.get(indexOfMax2ndDifference),
				estimatedPrice.get(indexOfMax2ndDifference+1),
				estimatedPrice.get(indexOfMax2ndDifference+2)
				);
		double average = StatsClass.getAverage(outlierCandidates);
		double maxDeviation = -1.0;
		int indexOfOutlier = -1;
		for(int i=0;i<outlierCandidates.size(); i++){
			double deviation = Math.pow(outlierCandidates.get(i) - average, 2.0);
			if(deviation > maxDeviation){
				maxDeviation = deviation;
				indexOfOutlier = i;
			}
		}
		int index = indexOfMax2ndDifference + indexOfOutlier;
		System.out.println("Outlier is " + estimatedPrice.get(index) + "\tIndex: " + (index));
		if(index > 0 && index < estimatedPrice.size()-1){
			estimatedPrice.set(index, 0.5*(estimatedPrice.get(index-1)+estimatedPrice.get(index+1)));
		}else if(index == 0){
			estimatedPrice.set(index, 2.0*estimatedPrice.get(index+1) - estimatedPrice.get(index+2));
		}else if(index == estimatedPrice.size()-1){
			estimatedPrice.set(index, 2.0*estimatedPrice.get(index-1) - estimatedPrice.get(index-2));
		}
		System.out.println(estimatedPrice);
		List<Double> coefsEstimatedPrice = GaussianCalculator.calculateCoefficients(estimatedPrice, 2);
		double afterr2 = GaussianCalculator.calculateR2(estimatedPrice, coefsEstimatedPrice);
		System.out.println("AFTER R2=" + afterr2);
		
		}
	}

}
