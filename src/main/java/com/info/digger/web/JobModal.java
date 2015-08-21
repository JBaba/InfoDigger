package com.info.digger.web;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class JobModal implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4622012925941316451L;
	
	private String title, location, salary, date, jobid, details, jobDescUrl;
	private List<String> skillList;
	
	public JobModal() {
		skillList = new LinkedList<String>();
	}
	
	public String getJobDescUrl() {
		return jobDescUrl;
	}

	public void setJobDescUrl(String jobUrl) {
		this.jobDescUrl = jobUrl;
	}

	public List<String> getSkillList() {
		return skillList;
	}

	public void setSkillList(List<String> skillList) {
		this.skillList = skillList;
	}

	public void addSkills(String job){
		if(!skillList.contains(job.toLowerCase()))
			skillList.add(job.toLowerCase());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	@Override
	public String toString() {
		StringBuffer myOut = new StringBuffer();
		
		myOut.append("Title: "+getTitle());
		myOut.append("\n");

		// Location
		myOut.append("Location: "+getLocation());
		myOut.append("\n");

		// wage
		myOut.append("Salary: "+getSalary());
		myOut.append("\n");

		// posted
		myOut.append("Date: "+getDate());
		myOut.append("\n");

		// List of skill
		myOut.append("Skill List: "+skillList.toString());
		myOut.append("\n");
		
		// Job id
		myOut.append("Job Id: "+getJobid());
		myOut.append("\n");
		
		// Details
		myOut.append("Details: "+getDetails());
		myOut.append("\n");
		
		// Job Desc Url
		myOut.append("Job Desc Url: "+getJobDescUrl());
		myOut.append("\n");
		
		return myOut.toString();
	}
}
