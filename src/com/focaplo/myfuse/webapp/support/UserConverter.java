package com.focaplo.myfuse.webapp.support;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.UserService;
@Component(value="userConverter")
public class UserConverter extends PropertyEditorSupport {
	@Autowired
	private UserService userManager;
	

	
	public void setUserManager(UserService userManager) {
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
