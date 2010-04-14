package com.focaplo.myfuse.webapp.spring;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.FileSystemResource;



public class DynDataSourceRegister implements BeanFactoryPostProcessor, ApplicationContextAware {
	protected final Log log = LogFactory.getLog(getClass());
	private ApplicationContext context;
	ConfigurableListableBeanFactory beanFactory;
	private String scanRootDirectory;
	
	public void setScanRootDirectory(String scanRootDirectory) {
		this.scanRootDirectory = scanRootDirectory;
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.context = arg0;
	}

	public List<String> searchSpringContextFiles(String dir){
		log.debug("scanning " + dir + " ...");
		List<String> results = new ArrayList<String>();
		File rootDirectory = new File(dir);
		if(!rootDirectory.isDirectory() || !rootDirectory.canRead()){
			log.error("Error: failed to scan client directories - root directory is either not there or can not be read, check " + scanRootDirectory);
			throw new RuntimeException("Failed to scan " + scanRootDirectory);
		}
		File[] labDirectories = rootDirectory.listFiles();
		for(File labDir:labDirectories){
			log.debug("checking sub-directory " + labDir.getName() + " ...");
			if(!labDir.isDirectory()){
				log.info("skipping " + labDir.getName() + " ...since it is not directory");
				continue;
			}
			if(!labDir.canRead()){
				log.info("skipping " + labDir.getName() +  " ...because it is not readable!! you might need to check this.");
				continue;
			}
			if(labDir.getName().endsWith("canceled")){
				log.info("skipping " + labDir.getName() +  " ...because it is canceled client");
				continue;
			}
			//search the applicationConetxt_*.xml file
			File[] files = labDir.listFiles();
			for(File f:files){
				if(f.canRead() && f.isFile() && f.getName().endsWith(".xml") && f.getName().startsWith("applicationContext-")){
					results.add(f.getAbsolutePath());
				}
				if(f.canRead() && f.isFile() && f.getName().equalsIgnoreCase("jdbc.properties")){
					results.add(f.getAbsolutePath());
				}
			}
		}
		return results;
	}
	public void scanAndRegisterExistingLabDataSources(String dir){
		List<String> files = this.searchSpringContextFiles(dir);
		for(String f:files){
			if(f.endsWith(".xml")){
				XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(f), this.context);
				log.info("found spring configuration file " + f + " and put in application context!");
			}else if(f.endsWith("jdbc.properties")){
				//load data source property file
				this.addDataSource(f);
			}
		}
	}
	
	public void addDataSource(String propertyFilePath){
		log.info("found customer jdbc data source properties at " + propertyFilePath + ", add to the routing data source...");
		Properties p = new Properties();
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(propertyFilePath);
			p.load(fis);
			//programmingly add to the bean factory and the user-routing-data-source
			this.addDataSource(p);
		}catch(Exception e){
			log.error("error",e);
			throw new RuntimeException(e);
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					log.error("error", e);
				}
				fis=null;
			}
		}
	}
	public void addDataSource(Properties p){
		this.addDataSource(p.getProperty("jdbc.labName"), p.getProperty("jdbc.driverClassName"), p.getProperty("jdbc.url"), p.getProperty("jdbc.username"), p.getProperty("jdbc.password"));
	}
	public void addDataSource(String labName, String driverClass, String url, String userName,String password){
		log.info("adding data source:" + labName +  " " + driverClass + " " + url + " " + userName);
		BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)beanFactory;
		RootBeanDefinition beanDef = new RootBeanDefinition();
		beanDef.setBeanClassName("org.apache.commons.dbcp.BasicDataSource");
		MutablePropertyValues properties = new MutablePropertyValues();
		properties.addPropertyValue("driverClassName", driverClass);
		properties.addPropertyValue("url",url);
		properties.addPropertyValue("username", userName);
		properties.addPropertyValue("password", password);
		beanDef.setPropertyValues(properties);
		String beanName = labName+"DataSource";
		beanRegistry.registerBeanDefinition(beanName, beanDef);
		//add to the routing data source
		UserRoutingDataSource dataSource = (UserRoutingDataSource)context.getBean("dataSource");
		DataSource newDataSource = (DataSource) this.beanFactory.getBean(beanName);
		log.info("Found the newly added data source " + newDataSource);
		dataSource.addTargetDataSource(labName, newDataSource);
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf)
			throws BeansException {
		this.beanFactory=bf;
		//scan customer data sources
		this.scanAndRegisterExistingLabDataSources(scanRootDirectory);
	}

	public void removeDataSource(String labName) {
		UserRoutingDataSource dataSource = (UserRoutingDataSource)context.getBean("dataSource");
		dataSource.removeTargetDataSource(labName);
	}
}
