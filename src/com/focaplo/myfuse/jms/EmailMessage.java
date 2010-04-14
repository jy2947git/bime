package com.focaplo.myfuse.jms;

import org.springframework.mail.SimpleMailMessage;

public class EmailMessage extends AbstractMessage {
	private SimpleMailMessage simpleMailMessage;

	public SimpleMailMessage getSimpleMailMessage() {
		return simpleMailMessage;
	}

	public void setSimpleMailMessage(SimpleMailMessage simpleMailMessage) {
		this.simpleMailMessage = simpleMailMessage;
	}
	
}
