package com.focaplo.myfuse.service;

import java.util.List;

public interface DataSourceService {

	public abstract void addDataSource(String labName, String driverClass,
			String url, String userName, String password);

	public abstract void removeDataSource(String labName);

	public abstract void scan();
	
	public abstract List<String> listLoadedDataSources();

}