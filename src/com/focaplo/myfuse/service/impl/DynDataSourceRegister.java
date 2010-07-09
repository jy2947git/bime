package com.focaplo.myfuse.service.impl;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import name.pachler.nio.file.ClosedWatchServiceException;
import name.pachler.nio.file.FileSystems;
import name.pachler.nio.file.Path;
import name.pachler.nio.file.Paths;
import name.pachler.nio.file.StandardWatchEventKind;
import name.pachler.nio.file.WatchEvent;
import name.pachler.nio.file.WatchKey;
import name.pachler.nio.file.WatchService;

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
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.focaplo.myfuse.service.ConfigurationService;
import com.focaplo.myfuse.service.DataSourceService;
import com.focaplo.myfuse.webapp.spring.UserRoutingDataSource;



@Service(value="dynDataSourceRegister")
public class DynDataSourceRegister implements BeanFactoryPostProcessor, ApplicationContextAware, DataSourceService, ApplicationListener  {
	protected final Log log = LogFactory.getLog(getClass());
	private ApplicationContext context;
	ConfigurableListableBeanFactory beanFactory;
//	@Autowired
//	@Qualifier("bimeConfiguration")
	//autowired not effective when it implements the BeanFactoryPostProcessor
	ConfigurationService bimeConfiguration;

	WatchService watchService;
	List<String> dataSources = new ArrayList<String>();
	
	public void setBimeConfiguration(ConfigurationService bimeConfiguration) {
		this.bimeConfiguration = bimeConfiguration;
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.context = arg0;
	}

	protected List<String> searchSpringContextFiles(String dir){
		log.debug("scanning " + dir + " ...");
		List<String> results = new ArrayList<String>();
		File rootDirectory = new File(dir);
		if(!rootDirectory.isDirectory() || !rootDirectory.canRead()){
			log.error("Error: failed to scan client directories - root directory is either not there or can not be read, check " + bimeConfiguration.getLabshome());
			throw new RuntimeException("Failed to scan " + bimeConfiguration.getLabshome());
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
	protected void scanAndRegisterExistingLabDataSources(String dir){
		List<String> files = this.searchSpringContextFiles(dir);
		for(String f:files){
			if(f.endsWith(".xml")){
				XmlBeanFactory xmlBeanFactory = new XmlBeanFactory(new FileSystemResource(f), this.context);
				log.info("found spring configuration file " + f + " and put in application context!");
			}else if(f.endsWith("jdbc.properties")){
				//load data source property file
				this.addDataSourceFromPropertyFile(f);
			}
		}
	}
	
	public void addDataSourceFromPropertyFile(String propertyFilePath){
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
	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.DataSourceService#addDataSource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
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
		dataSources.add(labName);
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf)
			throws BeansException {
		this.beanFactory=bf;
		this.setBimeConfiguration(this.context.getBean(ConfigurationService.class));
		this.scan();
		//register directory/file change listener
		this.registerFileChangeListener(bimeConfiguration.getLabshome());
	}

	class DataSourceFileMonitor implements Runnable{
		DynDataSourceRegister caller;
		WatchService monitor;
		String watchedPath;
		
		public DataSourceFileMonitor(
				DynDataSourceRegister dynDataSourceRegister,
				WatchService watchService,
				String path) {
			caller = dynDataSourceRegister;
			monitor=watchService;
			watchedPath=path;
		}

		@Override
		public void run() {
			for(;;){
			    // take() will block until a file has been created/deleted
			    WatchKey signalledKey;
			    try {
			    	if(monitor==null){
			    		log.info("watch service is down, stopping the thread...");
			    		throw new ClosedWatchServiceException();
			    	}
			        signalledKey = monitor.take();
			    } catch (InterruptedException ix){
			        // we'll ignore being interrupted
			        continue;
			    } catch (ClosedWatchServiceException cwse){
			        // other thread closed watch service
			        log.info("watch service closed, terminating.");
			        break;
			    }

			    // get list of events from key
			    List<WatchEvent<?>> list = signalledKey.pollEvents();

			    // VERY IMPORTANT! call reset() AFTER pollEvents() to allow the
			    // key to be reported again by the watch service
			    signalledKey.reset();

			    // we'll simply print what has happened; real applications
			    // will do something more sensible here
			    for(WatchEvent e : list){
			        String message = "";
			        if(e.kind() == StandardWatchEventKind.ENTRY_CREATE){
			            Path context = (Path)e.context();
			            message = context.toString() + " created";
			            
			            caller.addDataSourceFromPropertyFile(watchedPath+File.separator+context.toString()+File.separator+"jdbc.properties");
			        } else if(e.kind() == StandardWatchEventKind.ENTRY_DELETE){
			            Path context = (Path)e.context();
			            message = context.toString() + " deleted";
			            caller.removeDataSource(context.toString());
			        } else if(e.kind() == StandardWatchEventKind.OVERFLOW){
			            message = "OVERFLOW: more changes happened than we could retreive";
			        }
			        log.info(message);
			    }
			}
			
		}
		
	}
	protected void registerFileChangeListener(String labsHomeDir) {
		// register to detect any change under the bime lab home directory - to dynamically
		// add or remove lab data-source
		watchService = FileSystems.getDefault().newWatchService();
		Path watchedPath = Paths.get(labsHomeDir);
		WatchKey key = null;
		try {
		    key = watchedPath.register(watchService, StandardWatchEventKind.ENTRY_CREATE, StandardWatchEventKind.ENTRY_DELETE);
		    //start a new thread to respond to the file-create-delete events
		    ExecutorService es = Executors.newFixedThreadPool(1);
		    Future future = es.submit(new DataSourceFileMonitor(this, watchService, labsHomeDir));
		    
		} catch (UnsupportedOperationException uox){
		    log.error("file watching not supported!", uox);
		    // FIXME need to notify the system monitor that dyn-data-source-detection not working!!!
		}catch (IOException iox){
			log.error("I/O errors", iox);
			// FIXME need to notify the system monitor that dyn-data-source-detection not working!!!
		}
		
	}

	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.DataSourceService#removeDataSource(java.lang.String)
	 */
	public void removeDataSource(String labName) {
		UserRoutingDataSource dataSource = (UserRoutingDataSource)context.getBean("dataSource");
		dataSource.removeTargetDataSource(labName);
		dataSources.remove(labName);
		log.info("removed datasource:" + labName);
	}
	
	/* (non-Javadoc)
	 * @see com.focaplo.myfuse.service.impl.DataSourceService#scan()
	 */
	public void scan(){
		//scan customer data sources
		this.scanAndRegisterExistingLabDataSources(bimeConfiguration.getLabshome());
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if(event instanceof ContextClosedEvent){
			//close the datasource-file-monitor by shutting down the file-service
			log.info("shutting down the datasource file monitor...");
			if(watchService!=null){
				try {
					watchService.close();
				} catch (IOException e) {
					log.error(e);
				} finally{
					watchService=null;
					log.info("Watch service down");
				}
			}
			
		}
		
	}

	@Override
	public List<String> listLoadedDataSources() {
		return this.dataSources;
	}
}
