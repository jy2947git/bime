package com.focaplo.myfuse.service.impl;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.focaplo.myfuse.service.CommandService;
import com.focaplo.myfuse.service.SpringCommand;

@Service(value="incomingCommandsListener")
public class IncomingCommandsListener implements CommandService, ApplicationContextAware{
	Log log = LogFactory.getLog(getClass());
	ApplicationContext context;
	
	public void exec(String commandStringInJson){
		log.debug("Incoming:" + commandStringInJson);
		String res = "";
		//convert the json string to beanname, method name, and arguments
		//JSON the list
		ObjectMapper mapper = new ObjectMapper();
		try {
			SpringCommand sc = mapper.readValue(commandStringInJson, SpringCommand.class);
			Object service = context.getBean(sc.getBeanName());
			//run the service with method and arguments
			if(service!=null){
				Method[] methods = ReflectionUtils.getAllDeclaredMethods(service.getClass());
				Method m = null;
				for(Method method:methods){
					if(method.getName().equals(sc.getMethodName())){
						m = method;
						break;
					}
				}
				if(m!=null){
					//invoke
					Object result = ReflectionUtils.invokeMethod(m, service, new Object[]{sc.getArguments()});
					res = mapper.writeValueAsString(result);
				}else{
					throw new RuntimeException("can not find a method with name:" + sc.getMethodName() + " in class:" + service.getClass());
				}
			}else{
				throw new RuntimeException("Could not find spring command bean with name " + sc.getBeanName());
				
			}
		} catch (Exception e) {
			log.error("jms",e);
			throw new RuntimeException("Failed to execute spring command from [" + commandStringInJson+"]");
		} 
		log.debug("result:" + res);
		
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		log.info("incoming commands listener is starting up...");
		context=arg0;
	}
}
