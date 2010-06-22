package com.focaplo.myfuse.service.impl;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.focaplo.myfuse.service.ConfigurationService;

@Service(value="bimeConfiguration")
public class BimeConfiguration implements ConfigurationService {
	 protected final Log log = LogFactory.getLog(getClass());
	public static String propertyFileName = "bime.properties";
//	private static BimeConfiguration instance;
	
	//place-holder of bime properties
	private String bimehome;
	private String labshome;
	private String bimecontrollerfile;
	private String bimesupporttoaddress;
	private String bimesupportccaddress;
	

	
	private boolean isLogToLocal=true;
	private boolean isLogToQueue;
	private boolean isEmailThroughLocal=true;
	private boolean isEmailThroughQueue;
	private boolean isLocalStorage=true;
	private boolean isAmazonStorage;
	private boolean isCached;
	
	
	public BimeConfiguration() {
		super();
		try {
			this.load(propertyFileName);
		} catch (ConfigurationException e) {
			log.error(e);
		}
	}
	
//	public BimeConfiguration(String propertyFileName) throws ConfigurationException{
//		super();
//		this.load(propertyFileName);
//	}
	
//	public static BimeConfiguration instance(){
//		if(instance==null){
//			try {
//				instance = new BimeConfiguration("bime.properties");
//			} catch (ConfigurationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return instance;
//	}
	
	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#load(java.lang.String)
	 */
	public void load(String propertyFile) throws ConfigurationException{
		PropertiesConfiguration pc = new PropertiesConfiguration(propertyFile);
		bimehome = pc.getString("bime.home", "/usr/local/bime-home");
		labshome = System.getProperty("user.home");
		bimecontrollerfile = pc.getString("bime.controller.file",bimehome+java.io.File.separator+"bime-controller.ip");
		bimesupporttoaddress = pc.getString("bime.support.to.address","support.bimelab.com");
		bimesupportccaddress = pc.getString("bime.support.cc.address","support.bimelab.com");
		isLogToLocal = pc.getString("bime.log.mode","local").equalsIgnoreCase("local");
		isLogToQueue = pc.getString("bime.log.mode","local").equalsIgnoreCase("queue");
		isEmailThroughLocal = pc.getString("bime.email.mode","local").equalsIgnoreCase("local");
		isEmailThroughQueue = pc.getString("bime.email.mode","local").equalsIgnoreCase("queue");
		isLocalStorage = pc.getString("bime.file.storage","local").equalsIgnoreCase("local");
		isAmazonStorage = pc.getString("bime.file.storage","local").equalsIgnoreCase("s3");
		isCached = pc.getBoolean("bime.cached", false);
	}


	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#isLogToLocal()
	 */
	public boolean isLogToLocal() {
		return isLogToLocal;
	}
	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#isLogToQueue()
	 */
	public boolean isLogToQueue() {
		return isLogToQueue;
	}
	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#isEmailThroughLocal()
	 */
	public boolean isEmailThroughLocal() {
		return isEmailThroughLocal;
	}
	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#isEmailThroughQueue()
	 */
	public boolean isEmailThroughQueue() {
		return isEmailThroughQueue;
	}
	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#isAmazonStorage()
	 */
	public boolean isAmazonStorage() {
		return isAmazonStorage;
	}
	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#isLocalStorage()
	 */
	public boolean isLocalStorage() {
		return isLocalStorage;
	}

	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#isCached()
	 */
	public boolean isCached() {
		return isCached;
	}

	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#getBimehome()
	 */
	public String getBimehome() {
		return bimehome;
	}

	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#getBimecontrollerfile()
	 */
	public String getBimecontrollerfile() {
		return bimecontrollerfile;
	}

	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#getBimesupporttoaddress()
	 */
	public String getBimesupporttoaddress() {
		return bimesupporttoaddress;
	}

	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#getBimesupportccaddress()
	 */
	public String getBimesupportccaddress() {
		return bimesupportccaddress;
	}

	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.ConfigurationService#getLabshome()
	 */
	public String getLabshome() {
		return labshome;
	}
	
}
