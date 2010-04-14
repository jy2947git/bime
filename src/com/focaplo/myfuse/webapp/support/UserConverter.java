package com.focaplo.myfuse.webapp.support;

import java.beans.PropertyEditorSupport;

import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.ProjectManager;
import com.focaplo.myfuse.service.UserManager;

public class UserConverter extends PropertyEditorSupport {
	private UserManager userManager;
	

	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	@Override
	public String getAsText() {
		User s = (User)this.getValue();
		if (s == null) {
            return null;
        } else {
            return s.getId().toString();
        }
	}

	@Override
	public void setAsText(String arg0) throws IllegalArgumentException {
		if(arg0==null || arg0.equalsIgnoreCase("")){
			setValue(null);
		}else{
		 setValue(this.userManager.get(User.class, Long.parseLong(arg0)));
		}
	}

}
