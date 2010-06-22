package com.focaplo.myfuse.service;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;




/**
 * this class reads the property file and load the optional Spring beans based on the properties
 * For example, it reads by defaul the bime.properies. If the property bime.local.queue is TRUE,
 * (which means you have a local queue set up and want to use the bime-local-commands-listener), this
 * class will load the applicationContext-messaging-incoming.xml.optional file
 *
 */
@Service
public class ExtendedSpringBeanRepository implements ApplicationContextAware, BeanFactoryPostProcessor {
	protected Log log = LogFactory.getLog(getClass());
	private ApplicationContext defaultContext;
	GenericApplicationContext entendedBeans;
	
//	@Autowired
//	@Qualifier("bimeConfiguration")
	//autowired not effective when it implements the BeanFactoryPostProcessor
	ConfigurationService bimeConfiguration;
	public void setBimeConfiguration(ConfigurationService bimeConfiguration) {
		this.bimeConfiguration = bimeConfiguration;
	}
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.defaultContext = (ApplicationContext) arg0;
		entendedBeans = new GenericApplicationContext();
	}
	
	public void checkAndLoadOptionalBeans(){
		//load optional beans
		try {
			this.loadOptinalBeans();
		} catch (ConfigurationException e) {
			log.error("spring", e);
			throw new RuntimeException(e);
		}
	}
	
	private void addBeansFromClasspathXmlFile(String fileName){
		log.info("before we have beans:" + entendedBeans.getBeanDefinitionCount());
		XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(entendedBeans);
		xmlReader.loadBeanDefinitions(new ClassPathResource(fileName));
		
		
	}
	
	private void loadOptinalBeans() throws ConfigurationException {
		log.info("now we have beans:" + defaultContext.getBeanDefinitionCount());

		if(bimeConfiguration.isLogToQueue()){
			//register the queue connection factory to application context
			//inject the queue connection factory to queue-sender
			//get the logger and turn on the queue-sending
		}
		entendedBeans.refresh();
		entendedBeans.setParent(defaultContext);
		log.info("after we have beans:" + entendedBeans.getBeanDefinitionCount());
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory arg0)
			throws BeansException {
		this.setBimeConfiguration(this.defaultContext.getBean(ConfigurationService.class));
		this.checkAndLoadOptionalBeans();
		
	}
	
	public Object getBean(String beanName){
		if(this.entendedBeans.containsBean(beanName)){
			return this.entendedBeans.getBean(beanName);
		}else{
			return defaultContext.getBean(beanName);
		}
	}

	
}
