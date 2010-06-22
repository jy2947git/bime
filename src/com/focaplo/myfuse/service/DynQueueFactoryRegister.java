package com.focaplo.myfuse.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.ConstructorArgumentValues.ValueHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Service;

/**
 * this class programmingly adds the messaging connection factory to the application context.
 * It can also be used to update the existing connection factory bean, for example, to change
 * the host and port.
 *
 */
@Service
public class DynQueueFactoryRegister  implements BeanFactoryPostProcessor, ApplicationContextAware{
	protected final Log log = LogFactory.getLog(getClass());
	private ApplicationContext context;
	ConfigurableListableBeanFactory beanFactory;
	public void postProcessBeanFactory(ConfigurableListableBeanFactory bf)
			throws BeansException {
		this.beanFactory=bf;
		
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.context = arg0;
		
	}

	/*
	 * 	<bean name="remoteConnectionFactory" class="org.hornetq.jms.client.HornetQConnectionFactory" >
		<constructor-arg>
			<bean class="org.hornetq.api.core.TransportConfiguration">
				<constructor-arg value="org.hornetq.integration.transports.netty.NettyConnectorFactory" />
				<constructor-arg>
					<map key-type="java.lang.String" value-type="java.lang.Object">
						<entry key="host" value="localhost"></entry>
						<entry key="port" value="5445"></entry>
					</map>
				</constructor-arg>
			</bean>
		</constructor-arg>
	</bean>
	
    <bean id="remoteCachingConnectionFactory"
          class="org.springframework.jms.connection.CachingConnectionFactory"
          destroy-method="destroy">

        <property name="targetConnectionFactory" ref="remoteConnectionFactory"/>

    </bean>
	 */
	public void registerQueueFactoryBean(String host, Integer port){
		removeQueueFactoryBeanIfExists();
		this.registerCachedConnectionFactory(host, port);
	}
	
	public void removeQueueFactoryBeanIfExists(){
		BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)beanFactory;
		if(beanRegistry.containsBeanDefinition("remoteCachingConnectionFactory")){
		beanRegistry.removeBeanDefinition("remoteCachingConnectionFactory");
		}
		if(beanRegistry.containsBeanDefinition("remoteConnectionFactory")){
			beanRegistry.removeBeanDefinition("remoteConnectionFactory");
		}
		if(beanRegistry.containsBeanDefinition("transportConfiguration")){
			beanRegistry.removeBeanDefinition("transportConfiguration");
		}
	}
	
	public void updateQueueConnectionFactory(String newHost, Integer newPort){
		removeQueueFactoryBeanIfExists();
		this.registerCachedConnectionFactory(newHost, newPort);
		
	}
	private RootBeanDefinition registerCachedConnectionFactory(String host, Integer port){
		RootBeanDefinition remoteCachingConnectionFactory = new RootBeanDefinition();
		remoteCachingConnectionFactory.setBeanClass(org.springframework.jms.connection.CachingConnectionFactory.class);
		remoteCachingConnectionFactory.setDestroyMethodName("destroy");
		MutablePropertyValues properties = new MutablePropertyValues();
		properties.add("targetConnectionFactory", this.registerConnectionFactory(host, port));
		remoteCachingConnectionFactory.setPropertyValues(properties);
		//register
		BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)beanFactory;
		beanRegistry.registerBeanDefinition("remoteCachingConnectionFactory", remoteCachingConnectionFactory);
		//
		return remoteCachingConnectionFactory;
	}
	private RootBeanDefinition registerConnectionFactory(String host, Integer port){
		RootBeanDefinition remoteConnectionFactory = new RootBeanDefinition();
		remoteConnectionFactory.setBeanClass(org.hornetq.jms.client.HornetQConnectionFactory.class);
		ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
		constructorArgumentValues.addGenericArgumentValue(this.registerTransportConfiguration(host, port));
		remoteConnectionFactory.setConstructorArgumentValues(constructorArgumentValues);
		//register
		BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)beanFactory;
		beanRegistry.registerBeanDefinition("remoteConnectionFactory", remoteConnectionFactory);
		//
		return remoteConnectionFactory;
	}
	private RootBeanDefinition registerTransportConfiguration(String host, Integer port){
		RootBeanDefinition transportConfiguration = new RootBeanDefinition();
		transportConfiguration.setBeanClass(org.hornetq.api.core.TransportConfiguration.class);
		ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
		constructorArgumentValues.addGenericArgumentValue(new ValueHolder("org.hornetq.integration.transports.netty.NettyConnectorFactory"));
		Map<String, Object> argumentsMap = new HashMap<String, Object>();
		argumentsMap.put("host", host);
		argumentsMap.put("port", port);
		constructorArgumentValues.addGenericArgumentValue(argumentsMap);
		transportConfiguration.setConstructorArgumentValues(constructorArgumentValues);
		//register
		BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)beanFactory;
		beanRegistry.registerBeanDefinition("transportConfiguration", transportConfiguration);
		//
		return transportConfiguration;
	}
	
	/*
	 * <bean id="messageListener" class="org.springframework.jms.listener.adapter.MessageListenerAdapter">
    	<property name="delegate" ref="incomingCommandsListener"/>
    	<property name="defaultListenerMethod" value="receive"/>
    		<!-- we don't want automatic message context extraction -->
    	<property name="messageConverter">
        	<null/>
    	</property>
		</bean>
	 * <bean id="jmsContainer" class="org.springframework.jms.listener.DefaultMessageListenerContainer">
    	<property name="connectionFactory" ref="connectionFactory"/>
    	<property name="messageListener" ref="messageListener" />
		</bean>
	 */
	private RootBeanDefinition registerMessageListener(String queueName, String delegateBeanName, String deletgateMethodName){
		RootBeanDefinition messageListener = new RootBeanDefinition();
		messageListener.setBeanClass(org.springframework.jms.listener.adapter.MessageListenerAdapter.class);
		
		MutablePropertyValues properties = new MutablePropertyValues();
		properties.add("delegate", this.beanFactory.getBeanDefinition(delegateBeanName));
		properties.add("defaultListenerMethod", deletgateMethodName);
//		properties.add("messageConverter", null);
		messageListener.setPropertyValues(properties);
		BeanDefinitionRegistry beanRegistry = (BeanDefinitionRegistry)beanFactory;
		beanRegistry.registerBeanDefinition(delegateBeanName+"MessageListener", messageListener);
		//register container
		RootBeanDefinition listenerContainer = new RootBeanDefinition();
		listenerContainer.setBeanClass(org.springframework.jms.listener.DefaultMessageListenerContainer.class);
		
		MutablePropertyValues listenerContainerProperties = new MutablePropertyValues();
		listenerContainerProperties.add("connectionFactory", this.beanFactory.getBeanDefinition("remoteCachingConnectionFactory"));
		listenerContainerProperties.add("messageListener",messageListener);
		listenerContainerProperties.add("destinationName",queueName);
		listenerContainer.setPropertyValues(listenerContainerProperties);
		beanRegistry.registerBeanDefinition(delegateBeanName+"MessageListenerContainer", listenerContainer);
		
		return listenerContainer;
	}
	
	public void startMessageListener(String queueName, String delegateBeanName, String deletgateMethodName){
		this.registerMessageListener(queueName, delegateBeanName, deletgateMethodName);
		//initialize it
		DefaultMessageListenerContainer container = (DefaultMessageListenerContainer) this.context.getBean(delegateBeanName+"MessageListenerContainer");
		container.initialize();
		container.start();
	}
}
