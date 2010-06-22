package com.focaplo.myfuse.service;

import org.springframework.stereotype.Service;

@Service(value="dummyManager")
public class DummyManager {

	public String doSomething(Object[] args){
		StringBuffer buf = new StringBuffer();
		for(Object o:args){
			buf.append(o+" ");
		}
		return buf.toString().trim();
	}
}
