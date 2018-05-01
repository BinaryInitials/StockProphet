package com.stockprophet.html;

public enum Column {
	RANKING(false, null, null, true),
	DIFF(false, null, null, true),
	SYMB(false, null, null, false),
	COMPANY(false, null, null, false),
	SCORE(true, 0, 100, true),
	ALGO(true, 0, 100, true),
	RIGID(true, 0, 100, true),
	TURB(true, 0, 100, true),
	MONTH1(true, -99, 99, true),
	MONTH3(true, -99, 99, true),
	MONTH6(true, -999,999, true),
	YEAR1(true, -999,999, true),
	MOMENT(true, -99, 99, true),
	INERT(true, -99, 99, true),
	STAB(true, 0, 99, true),
	CONF(true, 0, 99, true),
	CONS(true, 0, 99, true);
	
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
