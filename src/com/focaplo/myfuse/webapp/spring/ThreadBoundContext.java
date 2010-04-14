package com.focaplo.myfuse.webapp.spring;

public class ThreadBoundContext {
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();
	
	public static void setValue(String value){
		contextHolder.set(value);
	}
	
	public static String getValue(){
		return contextHolder.get();
	}
	
	public static void clearValue(){
		contextHolder.remove();
	}
	
}
