package com.stockprophet.web;

public enum Column {
	RANK(false, null, null, true, false),
	DIFF(false, null, null, true, false),
	SYMB(false, null, null, false, false),
	COMPANY(false, null, null, false, false),
	SECTOR(false, null, null, false, false),
	INDUSTRY(false, null, null, false, false),
	PRICE(false, null, null, true, false),
	
	MKTCAP(true, 0, 100, true, true),
	LINEAR(true, 0, 100, true, true),
	RIGID(true, 0, 100, true, true),
	GROWTH(true, 0, 100, true, true),
	
	MVA(true, -5, 5, true, false),
	BBAND(true, -5, 5, true, false),
	
	OIDR(true, 0, 10, true, false),
	MIDR(true, 0, 10, true, false),
	
	;
	private boolean isFilterable, isNumber, isRanking;
	Integer min = null, max = null;
	
	private Column(boolean isFilterable, Integer min, Integer max, boolean isNumber, boolean isRanking){
		this.isFilterable = isFilterable;
		this.min = min;
		this.max = max;
		this.isNumber = isNumber;
		this.isRanking = isRanking;
	}
	
	public boolean isFilterable(){
		return isFilterable;
	}
	
	public Integer getMax(){
		return max;
	}
	
	public Integer getMin(){
		return min;
	}
	
	public boolean isNumber(){
		return isNumber;
	}
	
	public boolean isRanking(){
		return isRanking;
	}

}
