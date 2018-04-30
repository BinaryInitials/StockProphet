package com.stockprophet.indexes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GenerationNASDAQ {
	public static final String URL_PRIMER = "ftp://ftp.nasdaqtrader.com/symboldirectory/nasdaqlisted.txt";
	
	public static void main(String[] args) throws IOException {
		Date tic = new Date();
		HashMap<String, String> map = getNasdaq();
		int index =0;
		for(String key : map.keySet()){
			index++;
			System.out.println(index+"\t" + key + "\t" + map.get(key));
		}
		Date toc = new Date();
		System.out.println((toc.getTime() - tic.getTime())/1000 + "sec");
	}
	
	public static HashMap<String, String> getNasdaq() {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			URL url = new URL(URL_PRIMER);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(url.openStream()));
			List<String> data = new ArrayList<String>();
			String inputLine = "";
			while ((inputLine = buffer.readLine()) != null){
				data.add(inputLine);
			}
			buffer.close();
			for(int i=1;i<data.size()-1; i++){
				String line = data.get(i).replaceAll(".*href=\"[^ ]+\">([^<]+)<.*","$1") + "|" + data.get(i-1).replaceAll("<[^>]+>", "");
				if(line.toLowerCase().contains("test stock")){
					continue;
				}
				map.put(line.split("\\|")[0], line.split("\\|")[1].replaceAll(",.*","").replaceAll(" -.*", "").replaceAll("&amp;","&"));
			}
			return map;
		} catch (IOException e) {
		}
		return map;
	}
}