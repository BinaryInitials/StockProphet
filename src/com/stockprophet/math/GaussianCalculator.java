package com.stockprophet.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Jama.Matrix;

public class GaussianCalculator {
	
	
	public static List<Double> normalizeTo0and1(List<Double> prices){
		//This assumes old is 0 and new is size()-1
		List<Double> normalized = new ArrayList<Double>();
		double first = prices.get(0);
		double last = prices.get(prices.size()-1);
		for(double price : prices)
			normalized.add((price-first)/Math.abs(last-first));
		return normalized;
	}
	
	public static List<Double> getCoefficientsFromVandermonde(double[][] x, double[] y){
		List<Double> coefficients = new ArrayList<Double>();
		Matrix a = new Matrix(x);
		Matrix b = new Matrix(y, y.length);
		Matrix c = (a.transpose().times(a)).inverse().times(a.transpose()).times(b);
		double[][] coef = c.getArray();
		for(double[] coefOne : coef)
			coefficients.add(coefOne[0]);
		return coefficients;
	}

	public static List<Double> calculateCoefficientsFromZeroToOneNormalized(List<Double> data, int degree){
		List<Double> dataNormalized = normalizeDataTo0And1(data);
		double[] matrixY = new double[data.size()];
		double[][] matrixX = new double[data.size()][degree];
		
		//Creating a Vandermonde Matrix
		for(int row = 0;row<data.size(); row++){
			for(int col=1;col<degree+1;col++)
				matrixX[row][col-1] = Math.pow(row/(0.0+data.size()-1),col);
			matrixY[row] = dataNormalized.get(row);
		}

		List<Double> coefficients = new ArrayList<Double>();
		coefficients.add(0.0);
		coefficients.addAll(getCoefficientsFromVandermonde(matrixX, matrixY));
		return coefficients;
	}
	
	
	
	
	public static List<Double> calculateCoefficientsForZeroNormalizedX(List<Double> data, int degree){
		List<Double> dataNormalized = normalizeDataTo0(data);
		double[] matrixY = new double[data.size()];
		double[][] matrixX = new double[data.size()][degree];
		
		//Creating a Vandermonde Matrix
		for(int row = 0;row<data.size(); row++){
			for(int col=1;col<degree+1;col++){
				matrixX[row][col-1] = Math.pow(row/(0.0+data.size()),col);
				System.out.print(String.format("%.3f", matrixX[row][col-1]) + "\t");
			}
			matrixY[row] = dataNormalized.get(row);
			System.out.println("|" + String.format("%.3f", matrixY[row]));
		}

		List<Double> coefficients = new ArrayList<Double>();
		coefficients.add(0.0);
		coefficients.addAll(getCoefficientsFromVandermonde(matrixX, matrixY));
		return coefficients;
	}
	
	public static List<Double> normalizeDataTo0(List<Double> data){
		List<Double> normalized = new ArrayList<Double>();
		for(double datum : data)
			normalized.add(datum/data.get(0)-1.0);
		return normalized;
	}

	public static List<Double> normalizeDataTo0And1(List<Double> data){
		List<Double> normalized = new ArrayList<Double>();
		for(double datum : data)
			normalized.add((datum-data.get(0))/Math.abs(data.get(data.size()-1)-data.get(0)));
		return normalized;
	}
	
	public static List<Double> calculateCoefficientsForZero(List<Double> data, int degree){
		double[] matrixY = new double[data.size()];
		double[][] matrixX = new double[data.size()][degree];

		
		//Creating a Vandermonde Matrix
		for(int row = 0;row<data.size(); row++){
			for(int col=1;col<degree+1;col++)
				matrixX[row][col-1] = Math.pow(row,col);
			matrixY[row] = data.get(row);
		}
		
		List<Double> coefficients = new ArrayList<Double>();
		coefficients.add(0.0);
		coefficients.addAll(getCoefficientsFromVandermonde(matrixX, matrixY));
		return coefficients;
	}
	public static List<Double> calculateCoefficients(List<Double> data, int degree){
		return calculateCoefficients(data, data.size(), degree);
	}
	
	public static List<Double> calculateCoefficients(List<Double> data, int maxDays, int degree){
		double[] matrixY = new double[maxDays];
		double[][] matrixX = new double[maxDays][degree+1];

		
		//Creating a Vandermonde Matrix
		for(int row = 0;row<maxDays; row++){
			for(int col=0;col<degree+1;col++)
				matrixX[row][col] = Math.pow(-row-1,col);
			matrixY[row] = data.get(row);
		}
		
		return getCoefficientsFromVandermonde(matrixX, matrixY);
	}
	
	public static double calculateR2NormalizedFromZeroToOne(List<Double> data, List<Double> coefs){
		List<Double> normalizedData = normalizeDataTo0And1(data);
		return 1.0 - calculateResidualsNormalized(normalizedData, coefs)/calculateSST(normalizedData);
	}
	public static double calculateR2Normalized(List<Double> data, List<Double> coefs){
		List<Double> normalizedData = normalizeDataTo0(data);
		return 1.0 - calculateResidualsNormalized(normalizedData, coefs)/calculateSST(normalizedData);
	}
	
	public static double calculateR2(List<Double> data, List<Double> coefs){
		return 1 - calculateResiduals(data, coefs)/calculateSST(data);
	}
	
	public static double calculateResidualsNormalized(List<Double> data, List<Double> coefs){
		double error = 0.0;
		List<Double> yhat = calculateYHatNormalized(data.size(), coefs);
		for(int i=0;i<data.size();i++)
			error += (data.get(i)- yhat.get(i))*(data.get(i)- yhat.get(i));
		return error;
	}
	
	public static double calculateResidualsNormX(List<Double> data, List<Double> coefs){
		double error = 0.0;
		List<Double> yhat = calculateYHatNormalized(data.size(), coefs);
		for(int i=0;i<data.size();i++)
			error += (data.get(i) - yhat.get(i)) * (data.get(i) - yhat.get(i));
		return error;
	}
	
	public static double calculateResiduals(List<Double> data, List<Double> coefs){
		double error = 0.0;
		List<Double> yhat = calculateYHat(-data.size(), -1, coefs);
		for(int i=0;i<data.size();i++)
			error += (data.get(i)- yhat.get(i))*(data.get(i)- yhat.get(i));
		return error;
	}
	
	public static double calculateSST(List<Double> data){
		double average = calculateAverage(data), sum = 0.0;
		for(double datum : data)
			sum += (average-datum)*(average-datum);
		return sum;
	}
	
	public static double calculateAverage(List<Double> data){
		double average = 0;
		for(double datum : data)
			average+= datum;
		return average/(0.0+data.size());
	}
	
	public static List<Double> calculateYHatNormalized(int size, List<Double> coefs){
		List<Double> yHat = new ArrayList<Double>();
		for(int datum = 0;datum<size;datum++){
			double sum = 0;
			int i=0;
			for(double coef : coefs)
				sum+= coef*Math.pow(datum/(0.0+size-1), i++);
			yHat.add(sum);
		}
		return yHat;
	}
	
	public static double calculateYHatAtT(int t, List<Double> coefs){
		double yHatAtT = 0.0;
		for(int power=0;power<coefs.size();power++)
			yHatAtT += coefs.get(power)*Math.pow(t, power);
		return yHatAtT;
	}
	
	public static List<Double> calculateYHat(int startingPoint, int endPoint, List<Double> coefs){
		List<Double> yHat = new ArrayList<Double>();
		for(int datum = startingPoint;datum<endPoint;datum++)
			yHat.add(calculateYHatAtT(datum, coefs));
		return yHat;
	}
	
	public static double calculateDerivativeAtOne(List<Double> coefs){
		double derivativeAtOne = 0.0;
		for(int i=1;i<coefs.size();i++)
			derivativeAtOne += i*coefs.get(i);
		return derivativeAtOne;
	}
	public static double calculateDerivative2AtOne(List<Double> coefs){
		double derivativeAtOne = 0.0;
		for(int i=2;i<coefs.size();i++)
			derivativeAtOne += i*(i-1)*coefs.get(i);
		return derivativeAtOne;
	}
	
	public static List<Double> calculateDerivative(List<Double> coefs, int size) {
		List<Double> derivative = new ArrayList<Double>();
		if(coefs.size() < 2)
			return new ArrayList<Double>(Collections.nCopies(size, 0.0));
		for(int i=0;i<size;i++){
			double sum = 0, x=i/(double)size;
			for(int j=1;j<coefs.size();j++)
				if(i == 0 && j == 1)
					sum+=coefs.get(j);
				else
					sum+=coefs.get(j)*j*Math.pow(x, j-1);
			derivative.add(sum);
		}
		return derivative;
	}

	public static List<Double> calculateDerivative2(List<Double> coefs, int size) {
		List<Double> derivative = new ArrayList<Double>();
		if(coefs.size() < 3)
			return new ArrayList<Double>(Collections.nCopies(size, 0.0));
		for(int i=0;i<size;i++){
			double sum = 0, x=i/(double)size;
			for(int j=2;j<coefs.size();j++)
				if(i == 0 && j == 2)
					sum+=coefs.get(j);
				else
					sum+=coefs.get(j)*j*(j-1)*Math.pow(x, j-2);
			derivative.add(sum);
		}
		return derivative;
	}
	
	public static double calculateNormalizedLength(List<Double> coefs, int size){
		double normLength = 0;
		for(int i=0;i<size-1;i++)
			normLength+=Math.sqrt((getYHatForX(coefs, i/(0.0+size)) - getYHatForX(coefs, (i+1)/(0.0+size))) * (getYHatForX(coefs, i/(0.0+size)) - getYHatForX(coefs, (i+1)/(0.0+size))) + 1/(0.0+size*size));
		return normLength/Math.sqrt(2.0);
	}
	
	public static double getYHatForX(List<Double> coefs, double x){
		double yHat = 0.0;
		for(int j=0;j<coefs.size();j++)
			yHat+=coefs.get(j)*Math.pow(x, j);
		return yHat;
	}
	
	public static double getAverageCurvature(List<Double> dF, List<Double> d2F) {
		double averageCurvature = 0.0;
		int size =  Math.min(dF.size(), d2F.size());
		for(int i=0;i<size; i++){
			averageCurvature += Math.abs(d2F.get(i))/Math.pow(1.0+dF.get(i)*dF.get(i),1.5);
		}
		return averageCurvature/(double)size;
	}
	
	public static List<Double> getCurvature(List<Double> dF, List<Double> d2F){
		List<Double> curvature = new ArrayList<Double>();
		int size =  Math.min(dF.size(), d2F.size());
		for(int i=0;i<size; i++)
			curvature.add(d2F.get(i)/Math.pow(1.0+dF.get(i)*dF.get(i),1.5));
		return curvature;
	}
	
	public static List<Double> numericalDerivative(List<Double> data){
		return numericalDerivative(data, true);
	}
	public static List<Double> numericalDerivative(List<Double> data, boolean direction){
		List<Double> derivative = new ArrayList<Double>();
		for(int i=0;i<data.size()-1;i++)
			if(direction)
				derivative.add(data.get(i+1)-data.get(i));
			else
				derivative.add(data.get(i)-data.get(i+1));
		return derivative;
	}
}	