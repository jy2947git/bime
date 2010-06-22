package com.focaplo.myfuse.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.focaplo.myfuse.service.CacheService;
import com.focaplo.myfuse.service.ConfigurationService;

@Service(value="memcachedManager")
public class MemcachedManager implements CacheService, BeanFactoryPostProcessor, ApplicationContextAware{
	protected final Log log = LogFactory.getLog(getClass());
	private static final String NAMESPACE = "BIME:04820";
	private boolean turnedOn = false;
	private int connectionsPerCacheServer=10;
	private static List<MemcachedClient> clients = new ArrayList<MemcachedClient>();
	private static Map<String,Integer> servers = new HashMap<String,Integer>();
//	@Autowired
	//autowired is not effective when it is implementing the bean-factorhy-post-processor
	ConfigurationService bimeConfiguration;
	public void setBimeConfiguration(ConfigurationService bimeConfiguration) {
		this.bimeConfiguration = bimeConfiguration;
	}
	
	public MemcachedManager() {
		super();
		
	}
	
	private static MemcachedManager instance;
	public static MemcachedManager instance(){
		if(instance==null){
			instance = new MemcachedManager();
			instance.turnOn();
		}
		return instance;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.focaplo.myfuse.service.impl.CacheService#set(java.lang.String,
	 * int, java.lang.Object)
	 */
	public void set(String key, int ttl, final Object o) {
		MemcachedClient c = this.getCache();
		if(c==null|| !c.isAlive()){
			log.error("could not get alive memcached client, are you sure the server is up?");
			return;
		}
		log.debug("add to cache:" + key + " " + o);
		try{
			c.set(NAMESPACE + key, ttl, o);
		}catch(Exception e){
			log.error(e);
//			c.shutdown(); dont shut down it otherwise it will never be used even the server is back
//			this.removeMemcachedClient(c);
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.focaplo.myfuse.service.impl.CacheService#get(java.lang.String)
	 */
	public Object get(String key) {
		MemcachedClient c = this.getCache();
		if(c==null || !c.isAlive()){
			log.error("could not get alive memcached client, are you sure the server is up?");
			return null;
		}
		try{
			Object o = c.get(NAMESPACE + key);
			if (o == null) {
				log.debug("Not in cache: " + key);
			} else {
				log.debug("load from cache KEY: " + key);
			}
			return o;
		}catch(Exception e){
			log.error(e);
//			c.shutdown();
//			this.removeMemcachedClient(c);
			return null;
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.focaplo.myfuse.service.impl.CacheService#delete(java.lang.String)
	 */
	public Object delete(String key) {
		MemcachedClient c = this.getCache();
		if(c==null|| !c.isAlive()){
			log.error("could not get alive memcached client, are you sure the server is up?");
			return null;
		}
		log.debug("remove from cache:" + key);
		try{
			return c.delete(NAMESPACE + key);
		}catch(Exception e){
			log.error(e);
//			c.shutdown();
//			this.removeMemcachedClient(c);
			return null;
		}
	}

	public MemcachedClient getCache() {
		MemcachedClient c = null;
		try {
			int i = (int) (Math.random() *connectionsPerCacheServer);
			c = clients.get(i);
		} catch (Exception e) {
			log.error(e);
		}
		return c;
	}

	public boolean isOn() {
		return turnedOn;
	}

	public void turnOff() {
		turnedOn = false;
		if (clients != null) {
			for (MemcachedClient mc : clients) {
				if (mc != null && mc.isAlive()) {
					mc.shutdown();
				}
			}
			clients = null;
		}
	}

	public void turnOn() {
		turnedOn = true;
		

	}



	public void addCacheServer(String host, Integer port) {

		for (int i = 0; i <=connectionsPerCacheServer; i++) {
			MemcachedClient c;
			try {
				c = new MemcachedClient(
						new BinaryConnectionFactory(), AddrUtil
								.getAddresses(host+":"+port));
				clients.add(c);
				this.servers.put(host, port);
			} catch (IOException e) {
				log.error("failed to add cache client to host:" + host + " port:" + port, e);
			}
			
		}
	}
	public Map<String, Integer> listCacheServers() {
		return servers;
	}
	public void removeCacheServer(String host, Integer port) {
		//no need to do anything here...memcached clinet will be able to handle the server failure
		
	}

//	public void removeMemcachedClient(MemcachedClient c){
//		this.clients.remove(c);
//	}
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException {
		this.setBimeConfiguration(beanFactory.getBean(ConfigurationService.class));
		if(this.bimeConfiguration.isCached()){
			this.addCacheServer("127.0.0.1", 11211);
			this.turnOn();
		}
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		// TODO Auto-generated method stub
		
	}

	
	
}
