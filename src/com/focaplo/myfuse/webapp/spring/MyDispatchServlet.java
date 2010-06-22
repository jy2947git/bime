package com.focaplo.myfuse.webapp.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

public class MyDispatchServlet extends DispatcherServlet {
	protected final Log log = LogFactory.getLog(getClass());
	@Override
	protected void render(ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
//		System.out.println("view:" + mv.getViewName());
//		String viewName = mv.getViewName();
//		if(viewName.endsWith(".html")){
//			mv.setViewName(viewName+"?" + System.currentTimeMillis());
//		}
//		log.debug("view changed to " + mv.getViewName() + " view:" + mv.getView());
		super.render(mv, request, response);
	}

}
