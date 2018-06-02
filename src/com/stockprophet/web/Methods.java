package com.stockprophet.web;

import java.util.HashMap;

public class Methods {
	
	public static HashMap<Integer, int[]> generateColorMap(){
		HashMap<Integer, int[]> colorMap = new HashMap<Integer, int[]>();
		int count = 10; 
		colorMap.put(COLOR_INCREMENT*count--, new int[]{250,250,250});	//WHITE
		colorMap.put(COLOR_INCREMENT*count--, new int[]{150,200,250});	//CYAN
		colorMap.put(COLOR_INCREMENT*count--, new int[]{0,50,250});	//BLUE
		colorMap.put(COLOR_INCREMENT*count--, new int[]{0,250,50});	//GREEN
		colorMap.put(COLOR_INCREMENT*count--, new int[]{150,250,50});	//CHARTREUSE
		colorMap.put(COLOR_INCREMENT*count--, new int[]{250,250,50});	//YELLOW
		colorMap.put(COLOR_INCREMENT*count--, new int[]{250,150,50});	//ORANGE
		colorMap.put(COLOR_INCREMENT*count--, new int[]{250,25,25});	//RED
		colorMap.put(COLOR_INCREMENT*count--, new int[]{150,15,15});	//BROWN
		colorMap.put(COLOR_INCREMENT*count--, new int[]{50,0,0});	//DARK BROWN
		colorMap.put(COLOR_INCREMENT*count--, new int[]{20,0,0});	//BLACK
		colorMap.put(COLOR_INCREMENT*count--, new int[]{0,0,0});	//BLACK
		return colorMap;
	}
	
public static int COLOR_INCREMENT = 10;
	
	public static String convertToColor(int[] rgb){
		String hexaColor = "";
		for(int color : rgb){
			String temp = Integer.toHexString(color).toUpperCase();
			hexaColor += temp.length() == 1 ? "0"+temp: temp;
		}
		return hexaColor;
	}
	
	public static int[] darken(int[] color, int level){
		int[] darker = new int[3];
		for(int i=0;i<darker.length;i++)
			darker[i] = darkenSingleChannel(color[i], level);
		return darker;
	}
	
	public static final int DARK_GRADIENT = 5; 
	public static int darkenSingleChannel(int color, int level){
		return color-DARK_GRADIENT*level > DARK_GRADIENT ? color-DARK_GRADIENT*level:DARK_GRADIENT;
	}
	
	public static int[] generateColorsFromColorMap(double metric, HashMap<Integer, int[]> colorMap){
		for(int key=90;key>=-COLOR_INCREMENT;key-=COLOR_INCREMENT){
			if(key < metric * 100){
				int[] rgb1 = colorMap.get(key+COLOR_INCREMENT);
				int[] rgb2 = colorMap.get(key);
				int[] rgb = new int[3];
				for(int i=0;i<3;i++)
					rgb[i] = (int)Math.round((rgb1[i] - rgb2[i])*(100.0*metric - key)/(double)COLOR_INCREMENT + rgb2[i]);
				return rgb;
			}
		}
		return new int[]{0,0,0};
	}

}
