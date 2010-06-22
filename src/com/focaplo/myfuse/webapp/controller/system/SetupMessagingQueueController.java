package com.focaplo.myfuse.webapp.controller.system;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;

import com.focaplo.myfuse.service.DynQueueFactoryRegister;

/**
 * this controller is used to set up the JMS queue connections.
 * In the Cloud environment, we can use this controller to dynamically change the queue
 * connection factory settings (host, port)
 * Any service replying on JMS will need to call this controller first
 * 
 *
 */
@Controller
public class SetupMessagingQueueController implements ApplicationContextAware{
	protected Log log = LogFactory.getLog(getClass());
	
	ApplicationContext context;
	
	@RequestMapping(value="/support/messaging/setupQueueConnection.html", method=RequestMethod.POST)
	@ResponseBody
	public String setupQueueConnection(@RequestParam(value="host") String host, @RequestParam(value="port") Integer port, HttpServletResponse response){
		try{
			DynQueueFactoryRegister register  = this.context.getBean(DynQueueFactoryRegister.class);
			register.registerQueueFactoryBean(host, port);
			response.setContentType("text/plain");
			return "done";
		}catch(Exception e){
			log.error(e);
			return "fail";
		}
	}
	
	@RequestMapping(value="/support/messaging/startUpListener.html", method=RequestMethod.POST)
	@ResponseBody
	public String startUpListener(@RequestParam(value="queueName") String queueName, @RequestParam(value="delegateBeanName") String delegateBeanName, @RequestParam(value="deletgateMethodName") String deletgateMethodName, HttpServletResponse response){
		try{
			DynQueueFactoryRegister register  =  this.context.getBean(DynQueueFactoryRegister.class);
			register.startMessageListener(queueName, delegateBeanName, deletgateMethodName);
			response.setContentType("text/plain");
			return "done";
		}catch(Exception e){
			log.error(e);
			return "fail";
		}
	}
	
	@RequestMapping(value="/support/messaging/showQueueConnection.html", method=RequestMethod.GET)
	@ResponseBody
	public String showQueueConnection(HttpServletResponse response){
		try{
			CachingConnectionFactory connectionFactory =  this.context.getBean(CachingConnectionFactory.class);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			String json =  mapper.writeValueAsString(connectionFactory);
			response.setContentType("application/json");
			return json;
		}catch(Exception e){
			log.error(e);
			return "fail";
		}
	}

	@RequestMapping(value="/support/messaging/showListeners.html", method=RequestMethod.GET)
	@ResponseBody
	public String showListeners(HttpServletResponse response){
		try{
			Map<String, DefaultMessageListenerContainer> containers =  this.context.getBeansOfType(DefaultMessageListenerContainer.class);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			String json =  mapper.writeValueAsString(containers);
			response.setContentType("application/json");
			return json;
		}catch(Exception e){
			log.error(e);
			return "fail";
		}
	}
	
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		if(arg0 instanceof WebApplicationContext){
			context=arg0.getParent();
		}else{
			context = arg0;
		}
	}
}
