package com.focaplo.myfuse.webapp.support;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.service.ProjectService;

@Component(value="protocolConverter")
public class ExperimentProtocolConverter extends PropertyEditorSupport {
	@Autowired
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
