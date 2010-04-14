package com.focaplo.myfuse.jms;



import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.service.LabManager;
import com.focaplo.myfuse.webapp.spring.DynDataSourceRegister;

public class HookUpMysqlStorageMessageListener extends BimeMessageListener {
	@Autowired
	private HookUpMysqlStorageMessageReplySender replySender;
	@Autowired
	private DynDataSourceRegister dynDataSourceRegister;
	@Autowired
	private LabManager labManager;
	
	public void setLabManager(LabManager labManager) {
		this.labManager = labManager;
	}

	public void setDynDataSourceRegister(DynDataSourceRegister dynDataSourceRegister) {
		this.dynDataSourceRegister = dynDataSourceRegister;
	}
	@Override
	void handleObjectMessage(Message m) {
		log.debug("get message " + m);
		HookUpMysqlStorageMessage hm = (HookUpMysqlStorageMessage)m;
		String file = hm.getLabJdbcPropertyFilePath();
		log.info("adding new data source with:" + file);
		//FIXME password!
		this.dynDataSourceRegister.addDataSource(file);
		//create the Lab record and prepare storage for it
		Lab lab = new Lab();
		lab.setName(hm.getLabName());
		
		this.labManager.saveLab(lab);
		this.labManager.setupStorageForLab(lab);
		
		this.replySender.sendReplyToHookUpMysql(m.getMessageId(), "1.2.3.4", "ec1.amazon.com", lab.getName());
	}

	@Override
	void handleTextMessage(String m) {
		log.debug("get message " + m);
	}

	public void setReplySender(HookUpMysqlStorageMessageReplySender replySender) {
		this.replySender = replySender;
	}



}
