package com.focaplo.myfuse.jms;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext*.xml")
public class ExecutionUnitStatusMessageTest {
	@Autowired
	ExecutionUnitStatusSender statusSender;
	@Test
	public void testSend(){
		Set<HostedLab> hostedLabs = new HashSet<HostedLab>();
		hostedLabs.add(new HostedLab("lab1"));
		statusSender.sendStatus("1.2.3.4", "ec1.amazon.com", ExecutionUnitStatusMessage.class.getName()+System.currentTimeMillis(), "good", hostedLabs);
	}
	@Test
	public void testGoogleJson(){
		ExecutionUnitStatusMessage sm = new ExecutionUnitStatusMessage();
		sm.setIpAddress("1.2.3.4");
		sm.setMessageId(ExecutionUnitStatusMessage.class.getName()+System.currentTimeMillis());
		sm.setOk(true);
		sm.setUnitName("ec1.amazon.com");
		sm.addLab("lab1");
		sm.addLab("lab2");
		String t = sm.toString();
		System.out.println(t);
		//
		Gson gson = new Gson();
		
		ExecutionUnitStatusMessage s = gson.fromJson(t, ExecutionUnitStatusMessage.class);
		System.out.println(s.getIpAddress());
		System.out.println(s.getMessageId());
		System.out.println(s.getUnitName());
		for(HostedLab h:s.getHostedLabs()){
			System.out.println(h.getLabName());
		}
	}
}
