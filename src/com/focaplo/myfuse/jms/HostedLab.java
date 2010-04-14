package com.focaplo.myfuse.jms;

import java.io.Serializable;

import com.google.gson.Gson;

public class HostedLab implements Serializable {
	private String labName;
	
	public HostedLab(String labName) {
		super();
		this.labName = labName;
	}

	public HostedLab() {
		super();
		
	}

	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
		
	}

	public String getLabName() {
		return labName;
	}

	public void setLabName(String labName) {
		this.labName = labName;
	}
	
	
}
