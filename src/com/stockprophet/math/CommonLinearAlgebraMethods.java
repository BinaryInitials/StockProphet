package com.stockprophet.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import Jama.Matrix;

public class CommonLinearAlgebraMethods {
	
	public static List<Double> calculateCustomConsistency(List<Double> prices, int lookBackDays, int range){
		HashMap<String, List<Double>> stats = new HashMap<String, List<Double>>();
		stats.put("A", new ArrayList<Double>());
		stats.put("B", new ArrayList<Double>());
		stats.put("C", new ArrayList<Double>());
		stats.put("D", new ArrayList<Double>());
		stats.put("R", new ArrayList<Double>());
		
		for(int i=-range;i<range+1;i++){
			List<Double> subPrices = prices.subList(0, lookBackDays+i);
			List<Double> coefs = calculateCoefs(subPrices, 4);
			double r = calculateR2(subPrices, coefs);
			stats.get("D").add(coefs.get(0));
			stats.get("C").add(coefs.get(1));
			stats.get("B").add(coefs.get(2));
			stats.get("A").add(coefs.get(3));
			stats.get("R").add(r);
		}
		
		HashMap<String, Double> summarizedStat = new HashMap<String, Double>();
		for(String key : stats.keySet()){
			summarizedStat.put(key, StatsClass.getStandardDeviation(stats.get(key))/Math.abs(StatsClass.getAverage(stats.get(key))));
		}
		summarizedStat.put("AVGR", StatsClass.getAverage(stats.get("R")));
		
		double stability1 = summarizedStat.get("A") + summarizedStat.get("B") + summarizedStat.get("C") + summarizedStat.get("D");
		double stability2 = 3*summarizedStat.get("A") + 2*summarizedStat.get("B") + 1*summarizedStat.get("C");
		return Arrays.asList(stability1, stability2, summarizedStat.get("R"), summarizedStat.get("AVGR"));
	}
	
	private static List<Double> calculateCoefs(List<Double> data, int degree){
		double[] matrixY = new double[data.size()];
		double[][] matrixX = new double[data.size()][degree];

		
		//Creating a Vandermonde Matrix
		for(int row = 0;row<data.size(); row++){
			for(int col=0;col<degree;col++)
				matrixX[row][col] = Math.pow(-row-1,col);
			matrixY[row] = data.get(row);
		}
		
		List<Double> coefficients = new ArrayList<Double>();
//		coefficients.add(0.0);
		coefficients.addAll(getCoefficientsFromVandermonde(matrixX, matrixY, data.size()));
		return coefficients;
	}
	
	private static List<Double> getCoefficientsFromVandermonde(double[][] x, double[] y, int length){
		List<Double> coefficients = new ArrayList<Double>();
		Matrix a = new Matrix(x);
		Matrix b = new Matrix(y, length);
		Matrix c = (a.transpose().times(a)).inverse().times(a.transpose()).times(b);
		double[][] coef = c.getArray();
		for(double[] coefOne : coef)
			coefficients.add(coefOne[0]);
		return coefficients;
	}
	
	private static double calculateR2(List<Double> data, List<Double> coefs){
		return 1 - calculateResiduals(data, coefs)/calculateSST(data);
	}
	
	private static double calculateResiduals(List<Double> data, List<Double> coefs){
		double error = 0.0;
		List<Double> yhat = calculateYHat(data.size(), coefs);
		for(int i=0;i<data.size();i++)
			error += (data.get(i)- yhat.get(i))*(data.get(i)- yhat.get(i));
		return error;
	}
	
	private static double calculateSST(List<Double> data){
		double average = calculateAverage(data), sum = 0.0;
		for(double datum : data)
			sum += (average-datum)*(average-datum);
		return sum;
	}
	
	private static double calculateAverage(List<Double> data){
		double average = 0;
		for(double datum : data)
			average+= datum;
		return average/(0.0+data.size());
	}
	
	private static List<Double> calculateYHat(int size, List<Double> coefs){
		List<Double> yHat = new ArrayList<Double>();
		for(int datum = 0;datum<size;datum++){
			double sum = 0;
			int i=0;
			for(double coef : coefs)
				sum+= coef*Math.pow(-datum-1, i++);
			yHat.add(sum);
		}
		return yHat;
	}

}
