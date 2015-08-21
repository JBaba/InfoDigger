package com.info.digger.web;

import java.io.IOException;
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
		try {
			getInfo();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doneSignal.countDown();
	}

	private void getInfo() throws IOException {
		
		final JobModal job = new JobModal();
		String ans;
		
		Elements anchor = elemnt.getElementsByTag("a");
		ans=jobUrlObj.printFirstTagInnerHtmlFromList(anchor);
		job.setTitle(ans);

		// Location
		Elements location = elemnt.getElementsByClass("location");
		job.setLocation(location.text());

		// wage
		Elements wage = elemnt.getElementsByClass("wage");
		job.setSalary(wage.text());

		// posted
		Elements posted = elemnt.getElementsByClass("posted");
		job.setDate(posted.text());

		// List of skill
		Elements skill_items = elemnt.getElementsByClass("skill-item");
		for (Element skill_item : skill_items) {
			ans = jobUrlObj.printFirstTagInnerHtml(skill_item);
			job.addSkills(ans);
		}

		// Job desc in details
		final String jobdesUrl = jobUrlObj.getJobUrl() + jobUrlObj.getLink(anchor); // job Description url
		job.setJobDescUrl(jobdesUrl);
		
		/**
		 * executing retrieval logic of indv job into separate thread
		 */
		Runnable subJob = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					jobUrlObj.getInfoJobDescSetInJob(jobUrlObj.getHTML(jobdesUrl),job);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		new Thread(subJob).start();
		
		jobUrlObj.addJob(job);
		
	}
	
}
