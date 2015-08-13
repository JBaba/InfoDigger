package com.info.digger.web;

import java.util.concurrent.CountDownLatch;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WorkerJob implements Runnable{

	private final CountDownLatch doneSignal;
	private final Element elemnt;
	private final InvokeUrl jobUrlObj;

	WorkerJob(CountDownLatch doneSignal,Element elemnt,InvokeUrl jobUrl) {
		this.doneSignal = doneSignal;
		this.elemnt = elemnt;
		jobUrlObj=jobUrl;
	}

	@Override
	public void run() {
		getInfo();
		doneSignal.countDown();
	}

	private void getInfo() {
		
		JobModal job = new JobModal();
		String ans;
		
		//System.out.print("Title: ");
		Elements anchor = elemnt.getElementsByTag("a");
		ans=jobUrlObj.printFirstTagInnerHtmlFromList(anchor);
		job.setTitle(ans);
		//System.out.print("Href: ");

		// Location
		//System.out.print("Location: ");
		Elements location = elemnt.getElementsByClass("location");
		job.setLocation(location.text());
		//System.out.println(location.text());

		// wage
		//System.out.print("Salary: ");
		Elements wage = elemnt.getElementsByClass("wage");
		job.setSalary(wage.text());
		//System.out.println(wage.text());

		// posted
		//System.out.print("Date: ");
		Elements posted = elemnt.getElementsByClass("posted");
		job.setDate(posted.text());
		//System.out.println(posted.text());

		// List of skill
		//System.out.print("Skill List: ");
		Elements skill_items = elemnt.getElementsByClass("skill-item");
		for (Element skill_item : skill_items) {
			ans = jobUrlObj.printFirstTagInnerHtml(skill_item);
			job.addSkills(ans);
		}

		// Job desc in details
		String jobdesUrl = jobUrlObj.getJobUrl() + jobUrlObj.getLink(anchor); // job Description url
		job.setJobDescUrl(jobdesUrl);
		jobUrlObj.getInfoJobDescSetInJob(jobUrlObj.getHTML(jobdesUrl),job);
		
		jobUrlObj.addJob(job);
		
	}
	
}
