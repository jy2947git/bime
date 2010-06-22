package com.focaplo.myfuse.webapp.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

public class ThreadBoundFilter extends OncePerRequestFilter {
	protected final Log log = LogFactory.getLog(getClass());
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String host = request.getServerName();
		String labName = null;
		if(host.indexOf("localhost")>-1){
			labName="bime";
		}else{
			labName = host.substring(0, host.indexOf("."));
		}
//		log.debug("lab name is " + labName);
		ThreadBoundContext.setValue(labName);

		chain.doFilter(request, response);
		
	}

}
