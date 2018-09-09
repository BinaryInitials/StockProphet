package com.stockprophet.web;

import com.stockprophet.main.Run;

public enum Column {
	RANK(false, null, null, true, false),
	DIFF(false, null, null, true, false),
	SYMB(false, null, null, false, false),
	COMPANY(false, null, null, false, false),
	SECTOR(false, null, null, false, false),
	INDUSTRY(false, null, null, false, false),
	PRICE(true, 0, 1000000, true, false),
	PRED(true, 0, 1000000, true, false),
	VALUE(true, -100, 100, true, true),
	VELOCITY(true, -100, 100, true, false),
	N(true, 0, Run.TWO_YEARS, true, false),
	PCONF(true, 0, 100, true, true),
	MAXR2(true, 0, 100, true, true),
	NOISE(true, 0, 100, true, true),
	MKTCAP(true, 0, 100, true, false),
	LINEAR(true, 0, 100, true, true),
	RIGID(true, 0, 100, true, true),
	TURB(true, 0, 100, true, true),
	OIDR(true, 0, 10, true, true),
	MIDR(true, 0, 10, true, true),
	WEEK1(true, -100, 100, true, true),
	MONTH1(true, -100, 100, true, true),
	MONTH3(true, -100, 100, true, true),
	MONTH6(true, -1000,1000, true, true),
	YEAR1(true, -1000,1000, true, true),
	YEAR2(true, -10000,10000, true, true),
	STAB(true, 0, 100, true, true),
	CONF(true, 0, 100, true, true),
	CONS(true, 0, 100, true, true),
	SHARPE(true, -100, 100, true, true),
	WILLIAMS(true, -100,0,true, true);
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
