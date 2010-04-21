package com.focaplo.myfuse.webapp.support;

import java.beans.PropertyEditorSupport;

import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.service.ProjectService;

public class ExperimentProtocolConverter extends PropertyEditorSupport {
private ProjectService projectManager;
	

	public void setProjectManager(ProjectService projectManager) {
		this.projectManager = projectManager;
	}

	@Override
	public String getAsText() {
		ExperimentProtocol s = (ExperimentProtocol)this.getValue();
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
		 setValue(this.projectManager.get(ExperimentProtocol.class, Long.parseLong(arg0)));
		}
	}
}
