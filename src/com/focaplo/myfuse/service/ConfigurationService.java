package com.focaplo.myfuse.service;

import org.apache.commons.configuration.ConfigurationException;

public interface ConfigurationService {

	public abstract void load(String propertyFile)
			throws ConfigurationException;

	public abstract boolean isLogToLocal();

	public abstract boolean isLogToQueue();

	public abstract boolean isEmailThroughLocal();

	public abstract boolean isEmailThroughQueue();

	public abstract boolean isAmazonStorage();

	public abstract boolean isLocalStorage();

	public abstract boolean isCached();

	public abstract String getBimehome();

	public abstract String getBimecontrollerfile();

	public abstract String getBimesupporttoaddress();

	public abstract String getBimesupportccaddress();

	public abstract String getLabshome();

}