package com.stockprophet.indexes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GenerationNYSE {
	public static final String URL_PRIMER = "https://en.wikipedia.org/wiki/Companies_listed_on_the_New_York_Stock_Exchange_";
	
	public static HashMap<String, String> start(){
		HashMap<String, String> map = new HashMap<String, String>();
		displayPartialNYSE("0-9", map);
		for(int i=0;i<26;i++){
			displayPartialNYSE(""+(char)(i+65), map);
		}
		return map;
	}

	public static void main(String[] args) throws IOException {
		Date tic = new Date();
		HashMap<String, String> map = start();
		for(String key : map.keySet()){
			System.out.println(key + "\t" + map.get(key));
		}
		Date toc = new Date();
		System.out.println((toc.getTime() - tic.getTime())/1000 + "sec");
	}
	
	private static void displayPartialNYSE(String part, HashMap<String, String> map) {
		try {
			URL url = new URL(URL_PRIMER + "(" + part + ")");
			BufferedReader buffer = new BufferedReader(new InputStreamReader(url.openStream()));
			List<String> data = new ArrayList<String>();
			String inputLine = "";
			while ((inputLine = buffer.readLine()) != null){
				if(inputLine.matches("^<td>.*")){
					data.add(inputLine);
				}
			}
			buffer.close();
			for(int i=0;i<data.size(); i++){
				if(data.get(i).matches(".*href=\"[^ ]+\">[^<]+<.a><.td>.*")){
					String line = data.get(i).replaceAll(".*href=\"[^ ]+\">([^<]+)<.*","$1") + "|" + data.get(i-1).replaceAll("<[^>]+>", "");
					map.put(line.split("\\|")[0], line.split("\\|")[1].replaceAll("&amp;", "&"));
				}
			}
		} catch (IOException e) {
		}
	}
}