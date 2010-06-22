package com.focaplo.myfuse.webapp.spring;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.impl.MailEngine;
import com.focaplo.myfuse.webapp.util.RequestUtil;

public class MyExceptionHandler extends SimpleMappingExceptionResolver {
	Logger log = LogManager.getLogger(this.getClass());
	@Autowired 
	MailEngine mailEngine;
	String toAddress;
	String ccAddress;
	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		log.error("error",ex);
		try{
		this.sendNotification(request, ex);
		}catch(Exception e){
			log.error("Failed to send exception notification too!!!!");
		}
		return super.resolveException(request, response, handler, ex);
	}

	private void sendNotification(HttpServletRequest request, Exception ex) {
//		try{
//			//find login user
//			String user = "";
//			SecurityContext sc = SecurityContextHolder.getContext();
//			if(sc!=null){
//				Authentication au = sc.getAuthentication();
//				if(au!=null){
//					user = user + " " + au.getPrincipal();
//				}
//			}
//			
//			
//			SimpleMailMessage message = new SimpleMailMessage();
//			message.setSubject("Bime exception " + ex.getMessage());
//			message.setTo(this.toAddress);
//			message.setCc(this.ccAddress);
//			StringBuffer buf = new StringBuffer();
//			buf.append("\n" + user);
//			buf.append("\n" + new Date());
//			buf.append("\n" + request.getRequestURI() + " " + request.getQueryString());
//			buf.append("\n" + this.getCustomStackTrace(ex));
//			message.setText(buf.toString());
//			this.mailEngine.send(message);
//		}catch(Exception e){
//			log.error("failed to send notification");
//		}
	}
	
	public String getCustomStackTrace(Throwable aThrowable) {
	    //add the class name and any message passed to constructor
	    final StringBuilder result = new StringBuilder( "Shit happens!: " );
	    result.append(aThrowable.toString());
	    final String NEW_LINE = System.getProperty("line.separator");
	    result.append(NEW_LINE);

	    //add each element of the stack trace
	    for (StackTraceElement element : aThrowable.getStackTrace() ){
	      result.append( element );
	      result.append( NEW_LINE );
	    }
	    return result.toString();
	  }

}
