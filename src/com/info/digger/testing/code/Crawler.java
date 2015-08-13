package com.info.digger.testing.code;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler {

	public static void main(String[] args) throws InterruptedException, IOException {

		long tStart = System.currentTimeMillis();

		String a = null;
		a = "http://www.cybercoders.com/search/?page=1&searchterms=java&searchlocation=&newsearch=true&sorttype=date";

		Document doc = Jsoup.connect(a).get();
		System.out.println(doc.text());
		
		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;
		System.out.println("Time :" + elapsedSeconds);

	}
	
}
