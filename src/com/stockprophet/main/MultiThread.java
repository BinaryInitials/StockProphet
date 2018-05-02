package com.stockprophet.main;

public class MultiThread implements Runnable {

	private Thread thread;
	private String threadName;
	
	MultiThread(String name){
		threadName = name;
	}
	
	@Override
	public void run() {
		System.out.println(threadName + "\t" + GetPrice.run(threadName));
	}
	
	public void start(){
		if(thread == null){
			thread = new Thread(this, threadName);
			thread.start();
		}
	}

}
