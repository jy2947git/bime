package com.focaplo.myfuse.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class ProjectControllerInterceptor extends HandlerInterceptorAdapter {

	Class[] interceptedControllers = new Class[]{
			ProjectAccessFormController.class,
			ToDoListController.class,
			ToDoFormController.class,
			ExperimentNoteListController.class,
			ExperimentNoteFormController.class	
	};
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String handlerClass = handler.getClass().getName();
		boolean checkSession=false;
		for(int i=0;i<interceptedControllers.length;i++){
			if(handlerClass.equalsIgnoreCase(interceptedControllers[i].getName())){
				checkSession=true;
				break;
			}
		}
		if(!checkSession){
			return true;
		}
		HttpSession session = request.getSession();
		if(session==null){
			//redirect to login page
			response.sendRedirect("/login.html");
            return false;

		}
		Long projectId = (Long)session.getAttribute("projectId");
		if(projectId==null){
			//redirect to project list page
			response.sendRedirect("projectList.html");
            return false;

		}
		return true;
	}

}
