package com.info.digger.testing.code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Crawler {

	public static void main(String[] args) throws InterruptedException, IOException {

		Crawler c = new Crawler();
		System.setProperty("http.agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
		String a = null;
		a = "http://stackoverflow.com/questions/11227809/why-is-processing-a-sorted-array-faster-than-an-unsorted-array";

		c.getHTML(a);
		c.getHTMLJsoup(a);
		c.getHTML(a);

		c.getScanner(a);
		c.getHTMLJsoup(a);
	}

	public void getScanner(String a) {

		long tStart = System.currentTimeMillis();

		String content = null;
		URLConnection connection = null;
		try {
			connection = new URL(a).openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			StringBuffer html = new StringBuffer();
			while (scanner.hasNext()) {
				html.append(scanner.next());

			}

			Document doc = Jsoup.parse(html.toString()); // parsing

			long tEnd = System.currentTimeMillis();
			long tDelta = tEnd - tStart;
			double elapsedSeconds = tDelta / 1000.0;
			System.out.println("Time :" + elapsedSeconds);

			// System.out.println(html.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public Document getHTMLJsoup(String URL) throws IOException {
		Document doc = null;

		long tStart = System.currentTimeMillis();

		doc = Jsoup.connect(URL).get();
		// System.out.println(doc.text());

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;
		System.out.println("Time :" + elapsedSeconds);

		return doc;
	}

	public Document getHTML(String URL) {
		long tStart = System.currentTimeMillis();

		URL url;

		try {
			// get URL content
			url = new URL(URL);
			URLConnection conn = url.openConnection();
			StringBuffer html = new StringBuffer();

			// open the stream and put it into BufferedReader
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				html.append(inputLine);
			}
			br.close();

			Document doc = Jsoup.parse(html.toString()); // parsing

			long tEnd = System.currentTimeMillis();
			long tDelta = tEnd - tStart;
			double elapsedSeconds = tDelta / 1000.0;
			System.out.println("Time :" + elapsedSeconds);

			return doc;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
