package com.info.digger.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * This class is to retrieve jobs from cybercoders
 * @author nviradia
 *
 */
public class InvokeUrl {

	private String jobUrl = "http://www.cybercoders.com/";
	private String pageNum = "1";
	private String prePageNum = "0";
	private List<String> jobIds = null;
	private List<JobModal> jobs= null;

	public InvokeUrl() {
		
		System.setProperty("http.agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
		
		jobIds = new LinkedList<String>();
		jobs = new LinkedList<JobModal>();
	}
	
	/**
	 * add jobs
	 * @param job
	 */
	public void addJob(JobModal job){
		if(!jobs.contains(job))
			jobs.add(job);
	}

	/**
	 * get HTML page from URL
	 * @param URL
	 * @return
	 */
	public StringBuffer getHTML(String URL) {
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
			return html;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * conver html string to Document using Jsoup lib and retrive data
	 * @param html
	 * @return
	 * @throws InterruptedException
	 */
	public InvokeUrl getInfo(StringBuffer html) throws InterruptedException {
		Document doc = Jsoup.parse(html.toString()); // parsing 

		Elements content = doc.getElementsByClass("job-listing-item"); // Getting job item from main page
		
		// Parallel code execution for idivisual element
		CountDownLatch doneSignal = new CountDownLatch(content.size());
		ExecutorService executor = Executors.newFixedThreadPool(content.size());

		for (Element elemnt : content) {
			executor.execute(new WorkerJob(doneSignal, elemnt, this));
		}
		
		doneSignal.await();           // wait for all to finish
	    //System.out.println("Finished all threads");

		return this;
	}

	/**
	 * get Info for job desc
	 * @param jobHtml
	 * @return
	 */
	public InvokeUrl getInfoJobDesc(StringBuffer jobHtml) {
		Document doc = Jsoup.parse(jobHtml.toString()); // parsing 

		Elements content = doc.getElementsByClass("job-details"); // getting job desc
		//System.out.println(content.text());

		Elements job_id = doc.getElementsByClass("job-id"); // job id
		jobIds.add(job_id.text());
		//System.out.println(job_id.text());

		return this;
	}
	
	/**
	 * get info and set inside modal
	 * for job specific desc
	 * @param jobHtml
	 * @param job
	 * @return
	 */
	public InvokeUrl getInfoJobDescSetInJob(StringBuffer jobHtml,JobModal job) {
		Document doc = Jsoup.parse(jobHtml.toString()); // parsing

		Elements content = doc.getElementsByClass("job-details"); // getting job des
		job.setDetails(content.text());
		//System.out.println(content.text());

		Elements job_id = doc.getElementsByClass("job-id"); // job id
		job.setJobid(job_id.text());
		jobIds.add(job_id.text());
		//System.out.println(job_id.text());

		return this;
	}

	/**
	 * Retrieve link for job desc from anchor <a> tag
	 * @param anchor
	 * @return
	 */
	public String getLink(Elements anchor) {
		String linkText = null;
		for (Element link : anchor) {
			linkText = link.attr("href"); // get value from href attribute
			//System.out.println(linkText);
			break;
		}
		return linkText;
	}

	/**
	 * Retrieve value from element
	 * @param element
	 * @return
	 */
	public String printFirstTagInnerHtml(Element element) {
		String linkText = element.text();
		//System.out.println(linkText);
		return linkText;
	}

	/**
	 * This is list but just retrieve first value 
	 * @param elements
	 * @return
	 */
	public String printFirstTagInnerHtmlFromList(Elements elements) {
		for (Element link : elements) {
			String linkText = link.text(); // find first and return
			//System.out.println(linkText);
			return linkText;
		}
		return null;
	}

	/**
	 * print list
	 * @param jobIds
	 */
	@SuppressWarnings("rawtypes")
	public <TypeList extends List> void printJobIds(TypeList jobIds) {
		for (Object id : jobIds) {
			System.out.println(id);
		}
	}

	/**
	 * add separator
	 */
	public void printSeparator() {
		System.out.println("============================================================");
	}

	/**
	 * get maximum page size
	 * @param html
	 * @return
	 */
	public String getMaxPagerSize(StringBuffer html) {
		Document doc = Jsoup.parse(html.toString()); // parsing 
		String nextPageNum = pageNum;

		// Retrieve section which has pager or page number list with url
		Elements content = doc.getElementsByClass("page-item-number"); 
		for (Element elemnt : content) {
			nextPageNum = elemnt.text();
			//System.out.println(elemnt.text());
		}

		if ((Integer.parseInt(nextPageNum)) < (Integer.parseInt(prePageNum))) {
			System.out.println("No more pages.");
			return null;
		}

		prePageNum = nextPageNum;
		nextPageNum = ((Integer.parseInt(nextPageNum)) + 1) + "";
		String nextPageNumUrl = "http://www.cybercoders.com/search/?page=" + nextPageNum
				+ "&searchterms=java&searchlocation=&newsearch=true&sorttype=date";
		StringBuffer nexthtml = getHTML(nextPageNumUrl);
		getMaxPagerSize(nexthtml);

		return null;
	}

	/**
	 * print html page
	 * @param URL
	 * @return
	 */
	public InvokeUrl printHTML(String URL) {
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
				System.out.println(inputLine);
			}
			br.close();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}

	public static void main(String[] args) throws InterruptedException {

		long tStart = System.currentTimeMillis();

		String a = null;
		InvokeUrl invokeUrl = new InvokeUrl();
		a = "http://www.cybercoders.com/search/?page=1&searchterms=java&searchlocation=&newsearch=true&sorttype=date";
		invokeUrl.getInfo(invokeUrl.getHTML(a));
		invokeUrl.printSeparator();
		invokeUrl.printJobIds(invokeUrl.getJobs());

		long tEnd = System.currentTimeMillis();
		long tDelta = tEnd - tStart;
		double elapsedSeconds = tDelta / 1000.0;
		System.out.println("Time :" + elapsedSeconds);

		/*
		 * a =
		 * "http://www.cybercoders.com/search/?page=2&searchterms=java&searchlocation=&newsearch=true&sorttype=date";
		 * StringBuffer html = invokeUrl.getHTML(a);
		 * invokeUrl.getMaxPagerSize(html);
		 */
	}

	public String getJobUrl() {
		return jobUrl;
	}

	public void setJobUrl(String jobUrl) {
		this.jobUrl = jobUrl;
	}

	public List<JobModal> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobModal> jobs) {
		this.jobs = jobs;
	}

	
	
}
