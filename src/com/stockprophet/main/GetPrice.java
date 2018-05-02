package com.stockprophet.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class GetPrice {
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		String symbol = "AAPL";
		System.out.println(symbol + "\t" + run(symbol));

	}
	
	public static String run(String symbol){
		String url = "https://finance.yahoo.com/quote/" + symbol;
		String price = null;
		BufferedReader buffer;
		try {
			buffer = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
			String line;
			while((line=buffer.readLine()) != null){
				if(line.contains("root")){
					String[] lines = line.split(",");
					for(String part : lines){
						if(part.contains("regularMarketPrice")){
							price = part.replaceAll(".*raw.:", "");
							break;
						}
					}
				}
			}
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return price;
	}

}