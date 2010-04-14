package com.focaplo.myfuse.jms;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.Gson;

/**
 * this is the message the server instance routinely broadcast.
 *
 */
public class ExecutionUnitStatusMessage extends AbstractMessage{
	
	String unitName;
	private boolean ok;
	private String ipAddress;
	private Set<HostedLab> hostedLabs = new HashSet<HostedLab>();
	
	


	public void addLab(String labName){
		this.hostedLabs.add(new HostedLab(labName));
	}
	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Set<HostedLab> getHostedLabs() {
		return hostedLabs;
	}

	public void setHostedLabs(Set<HostedLab> hostedLabs) {
		this.hostedLabs = hostedLabs;
	}


	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
	
}
