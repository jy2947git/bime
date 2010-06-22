package com.focaplo.myfuse.service;

import java.util.List;
import java.util.Map;

public interface CacheService extends Switchable{

	public final int default_ttl_in_seconds = 3600;
	public abstract void set(String key, int ttl, final Object o);

	public abstract Object get(String key);

	public abstract Object delete(String key);

	public abstract void addCacheServer(String host, Integer port);
	
	public abstract void removeCacheServer(String host, Integer port);
	
	public abstract Map<String, Integer> listCacheServers();

}