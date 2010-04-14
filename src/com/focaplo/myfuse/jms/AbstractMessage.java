package com.focaplo.myfuse.jms;

import com.google.gson.Gson;

public class AbstractMessage implements Message {

	String messageId;
	
	@Override
	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
		
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}
