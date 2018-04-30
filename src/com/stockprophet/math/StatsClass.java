package com.stockprophet.math;

import java.util.ArrayList;
import java.util.List;

public class StatsClass {

	public static Double sumData(List<Double> data, int power){
		Double sum = 0d;
		for(double datum :data){
			sum  =sum + Math.pow(datum, power);
		}
		return sum;
	}
	public static Double sumData(List<Double> data){
		Double sum = 0d;
		for(double datum :data){
			sum  =sum + datum;
		}
		return sum;
	}
	
	public static double sumData(double[] data, int power){
		Double sum = 0d;
		for(double datum : data){
			sum  =sum + Math.pow(datum, power);
		}
		return sum;
	}

	
	public static int sumData(int[] data){
		int sum = 0;
		for(int datum : data){
			sum  =sum + datum;
		}
		return sum;
	}
	public static Double sumData(List<Double> data1, List<Double> data2, int power){
		Double sum = 0d;
		for(int i=0; i<data1.size();i++){
			sum  =sum + Math.pow(data1.get(i)*data2.get(i),power);
		}
		return sum;
	}
	
	public static Double getAverage(List<Double> data){
		return sumData(data,1)/data.size();
	}
	public static Double getStandardDeviation(List<Double> data){
		Double sum1 = sumData(data,1);
		Double sum2 = sumData(data,2);
		return Math.sqrt(sum2/data.size() - sum1*sum1/(data.size()*data.size()));
	}
	public static List<Double> createVector(int length){
		List<Double> vector = new ArrayList<Double>();
		for(int i=0;i<length;i++){
			vector.add((double)i);
		}
		return vector;
	}
	public static Double getR(List<Double> data){
		if(data != null){
			return (sumData(data, createVector(data.size()),1)/data.size() - getAverage(data)*getAverage(createVector(data.size())))/(getStandardDeviation(data)*getStandardDeviation(createVector(data.size())));
		}
		return null;
	}
}