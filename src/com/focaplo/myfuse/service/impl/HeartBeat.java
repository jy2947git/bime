package com.focaplo.myfuse.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.focaplo.myfuse.jms.ExecutionUnitStatusMessage;
import com.focaplo.myfuse.jms.ExecutionUnitStatusSender;
import com.focaplo.myfuse.jms.HostedLab;

public class HeartBeat {
	@Autowired
	ExecutionUnitStatusSender statusSender;
	
	public void sendHeartBeatMessage(){
		Set<HostedLab> hostedLabs = new HashSet<HostedLab>();
		hostedLabs.add(new HostedLab("lab1"));
		statusSender.sendStatus("1.2.3.4", "ec1.amazon.com", ExecutionUnitStatusMessage.class.getName()+System.currentTimeMillis(), "good", hostedLabs);

	}
}
