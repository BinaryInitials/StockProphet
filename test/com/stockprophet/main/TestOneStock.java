package com.stockprophet.main;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.stockprophet.common.Stock;
import com.stockprophet.common.StockUtil;
import com.stockprophet.html.Column;
import com.stockprophet.math.CommonLinearAlgebraMethods;
import com.stockprophet.math.GaussianCalculator;

public class TestOneStock {
	
	@Test
	public void testTSS(){
		String key = "TSS";
		List<Double> prices = StockUtil.getPriceFromFile(key);
	
		System.out.println(prices.size());
		List<Double> yest = prices.subList(1, 250-1+1);
		System.out.println(yest);
		Stock stock = new Stock(key, "", prices);
		System.out.println(yest);
		HashMap<Column, String> columnYesterday = Run.populateColumns(stock, 1);
		System.out.println(yest); //<--- The array has been modified
		System.out.println(columnYesterday.get(Column.YEAR1));
		System.out.println(yest);
		double first = yest.get(0);
		System.out.println(yest);
		double last =  yest.get(yest.size()-1);
		System.out.println("RECENT: " + first);
		System.out.println("LAST: " + last);
		System.out.println(yest);
		System.out.println("METHOD: " + StockUtil.calculateGrowth(yest));
		System.out.println("MANUAL: " + (first/last-1.0));
	}
	
	@Test
	public void testCoefficients(){
		String key = "TSS";
		List<Double> prices = StockUtil.getPriceFromFile(key);
		List<Double> coefs = GaussianCalculator.calculateCoefficients(prices, 3);
		System.out.println(coefs);
		Collections.reverse(prices);
		List<Double> consistencyMetrics	= CommonLinearAlgebraMethods.calculateCustomConsistency(prices, 30, 8);
		System.out.println(consistencyMetrics);
		System.out.println(Math.exp(-consistencyMetrics.get(0)/100));
		System.out.println(Math.exp(-consistencyMetrics.get(1)/200));
		System.out.println(Math.sqrt(Math.exp(-consistencyMetrics.get(0)/100)*Math.exp(-consistencyMetrics.get(1)/200)));
		
	}

}
