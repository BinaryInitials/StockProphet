package com.stockprophet.web;

public enum Column {
	RANK(false, null, null, true),
	DIFF(false, null, null, true),
	SYMB(false, null, null, false),
	COMPANY(false, null, null, false),
	SECTOR(false, null, null, false),
	INDUSTRY(false, null, null, false),
	PRICE(true, 0, 1000000, true),
	NOISE(true, 0, 100, true),
	MKTCAP(true, 0, 100, true),
	LINEAR(true, 0, 100, true),
	RIGID(true, 0, 100, true),
	TURB(true, 0, 100, true),
	OIDR(true, 0, 10, true),
	MIDR(true, 0, 10, true),
	WEEK1(true, -100, 100, true),
	MONTH1(true, -100, 100, true),
	MONTH3(true, -100, 100, true),
	MONTH6(true, -1000,1000, true),
	YEAR1(true, -1000,1000, true),
	YEAR3(true, -10000,10000, true),
	YEAR5(true, -10000,10000, true),
	MOMENT3(true, -1000, 1000, true),
	INERT3(true, -1000, 1000, true),
	MOMENT5(true, -1000, 1000, true),
	INERT5(true, -1000, 1000, true),
	STAB(true, 0, 100, true),
	CONF(true, 0, 100, true),
	CONS(true, 0, 100, true);
	
	private boolean isFilterable;
	private boolean isNumber;
	Integer min = null, max = null;
	
	private Column(boolean isFilterable, Integer min, Integer max, boolean isNumber){
		this.isFilterable = isFilterable;
		this.min = min;
		this.max = max;
		this.isNumber = isNumber;
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

}
