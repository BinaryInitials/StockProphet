package com.stockprophet.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.stockprophet.common.StockUtil;
import com.stockprophet.math.GaussianCalculator;

public class TestingMomentAndInertia {

	public static void main(String[] args) {
		String key = "ALGN";
		List<Double> prices = StockUtil.getPriceFromFile(key);
		
		List<Double> clone = new ArrayList<>();
		for(Double price : prices)
			clone.add(price);
		Collections.reverse(clone);
		System.out.println(clone);
		List<Double> normalized = GaussianCalculator.normalizeDataTo0(clone);
		System.out.println(normalized);
		List<Double> coefs5 = GaussianCalculator.calculateCoefficients(normalized, 5);
		System.out.println(coefs5);
		double moment5 = 1000*(
				5*coefs5.get(5)*Math.pow(clone.size(),  4.0) + 
				4*coefs5.get(4)*Math.pow(clone.size(), 3.0) + 
				3*coefs5.get(3)*Math.pow(clone.size(), 2.0) + 
				2*coefs5.get(2)*clone.size() +
				1*coefs5.get(1)
				);
		double inert5 = 1000000*(
				5*4*coefs5.get(5)*Math.pow(clone.size(), 3.0) + 
				4*3*coefs5.get(4)*Math.pow(clone.size(), 2.0) + 
				3*2*coefs5.get(3)*clone.size() + 
				2*1*coefs5.get(2)
				);
		
		System.out.println(moment5);
		System.out.println(inert5);

	}
}