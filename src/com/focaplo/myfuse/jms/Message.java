package com.focaplo.myfuse.jms;

import java.io.Serializable;

public interface Message extends Serializable{
	public String getMessageId();
	public void setMessageId(String id);
	
}
