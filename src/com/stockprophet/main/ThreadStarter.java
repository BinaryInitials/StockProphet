package com.stockprophet.main;

import java.util.Date;

public class ThreadStarter {

	public static void main(String[] args) {
		Date tic = new Date();
		MultiThread thread1 = new MultiThread("AAPL");
		MultiThread thread2 = new MultiThread("FB");
		MultiThread thread3 = new MultiThread("AMZN");
		MultiThread thread4 = new MultiThread("NFLX");
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		Date toc = new Date();
		
		System.out.println("Ellapsed time: " + (toc.getTime()-tic.getTime()) + "ms");
		
		System.out.println("AAPL\t" + GetPrice.run("AAPL"));
		System.out.println("FB\t" + GetPrice.run("FB"));
		System.out.println("AMZN\t" + GetPrice.run("AMZN"));
		System.out.println("NFLX\t" + GetPrice.run("NFLX"));
	}
}