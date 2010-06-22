package com.focaplo.myfuse.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.focaplo.myfuse.dao.ILabDao;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.model.LabMeetingItem;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.ConfigurationService;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.StorageService;

@Service(value="labManager")
@Transactional(readOnly=true)
public class LabManager extends UniversalManager implements LabService {
	@Autowired
	private ILabDao labDao;
	@Autowired
	@Qualifier("storageManager")
	private StorageService storageService;
	@Autowired
	private VelocityEngine velocityEngine;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	ConfigurationService bimeConfiguration;
	
	public void setBimeConfiguration(ConfigurationService bimeConfiguration) {
		this.bimeConfiguration = bimeConfiguration;
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}

	public void setLabDao(ILabDao labDao) {
		this.labDao = labDao;
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void saveMeeting(LabMeeting meeting) {
		this.universalDao.saveOrUpdate(meeting);
		Set<LabMeetingItem> items = meeting.getMeetingItems();
		for(LabMeetingItem item:items){
			this.universalDao.saveOrUpdate(item);
		}
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void updateMeetingItems(LabMeeting meeting) {
	}

	public List<User> getLabMembers() {
		return this.userDao.getUsers();
	}

	public List<LabMeetingItem> getMeetingItemsOfMeeting(Long meetingId) {
		return this.labDao.getMeetingItemsOfMeeting(meetingId);
	}

	public List<MeetingFile> getMeetingFilesOfMeeting(Long meetingId) {
		return this.labDao.getMeetingFilesOfMeeting(meetingId);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void removeLab(Long id) {
		Lab lab = (Lab) this.labDao.get(Lab.class, id);

		this.storageService.removeStorageForLab(lab);

		this.labDao.remove(Lab.class, id);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void setupStorageForLab(Lab lab) {
		this.storageService.prepareStorageForLab(lab);

		this.labDao.saveOrUpdate(lab);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void saveLab(Lab lab){
		this.labDao.saveOrUpdate(lab);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void uploadMeetingFile(Lab lab, MeetingFile meetingFile,
			ByteArrayInputStream is) {
		//first save the meeting file
		if(meetingFile.getId()!=null){
			throw new RuntimeException("meeting file has to be new object for uploading");
		}
		this.labDao.save(meetingFile);
		//now call the storage manager to upload file, which will also update the meeting file with real storage identifier
		this.storageService.storeFile(lab, meetingFile, is);
		//now update the meeting file
		log.info("after storing file, the storage identifier is " + meetingFile.getUniqueStorageIdentifier());
		this.labDao.saveOrUpdate(meetingFile);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void removeMeetingFile(Lab lab, MeetingFile meetingFile){
		if(meetingFile.getId()==null){
			throw new RuntimeException("meeting file has to be existing object for deleting");
		}
		if(meetingFile.getUniqueStorageIdentifier()!=null){
			this.storageService.removeFile(lab, meetingFile);
		}
		this.labDao.remove(MeetingFile.class, meetingFile.getId());
	}

	public Lab getLabByName(String labName) {
		Lab lab = this.labDao.getLabByname(labName);
		if(lab==null){
			lab = new Lab();
			lab.setName("bime");
			lab.setStorageIdentity("/bime");
		}
		return lab;
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void removeLabByName(String labName) {
		Lab lab = this.getLabByName(labName);
		//remove the storage
		if(lab.getStorageIdentity()!=null){
			this.storageService.removeStorageForLab(lab);
		}
		//remove the lab record
		this.labDao.remove(Lab.class, lab.getId());

	}

	public List<LabMeeting> getMeetingsInviting(Long id) {
		// get the meeting with the given id as parts
		List<LabMeeting> meetings = this.labDao.getMeetingsParticipatedByUser(id);
		//sort of date
		return meetings;
	}

	public String createNewLab(Long labId, String uniqueContextRoot, String labName, String adminName, String adminEmail, String adminPassword) throws IOException {
		//need to encrypt the password using the password encoder
		String encrypedAdminPassword = this.passwordEncoder.encodePassword(adminPassword, null);
		
		log.info("creating root directory for new lab:" + uniqueContextRoot);
		File rootDir =  new File(bimeConfiguration.getLabshome()+File.separator+uniqueContextRoot);
		if(rootDir.exists()&&rootDir.isDirectory()){
			throw new RuntimeException("root directory " + rootDir.getAbsolutePath() + " already exists");
			//try change name
//			if(!rootDir.renameTo(new File(BimeConfiguration.instance().getBimehome()+File.separator+uniqueContextRoot+"-"+System.currentTimeMillis()))){
//				throw new RuntimeException("Lab root directory aleady exists and coud not be renamed");
//			}
		}
		if(!rootDir.mkdir()){
			throw new RuntimeException("failed to create directory " + rootDir.getAbsolutePath());
		}
		
		int returnCode = -1;
		log.info("creating the jdbc.properties in the lab root directory:" + rootDir.getAbsolutePath());
		PrintStream p=null; // declare a print stream object
        try{
                p = new PrintStream(new FileOutputStream(bimeConfiguration.getLabshome() + "/" +uniqueContextRoot+"/jdbc.properties"));
                p.println("");
                p.println("jdbc.labName="+uniqueContextRoot);
                p.println("jdbc.driverClassName=com.mysql.jdbc.Driver");
                p.println("jdbc.url=jdbc:mysql://localhost/"+uniqueContextRoot);
                p.println("jdbc.username="+uniqueContextRoot);
                p.println("jdbc.password=" + adminPassword);
        }catch (Exception e){
                log.error("file",e);
                return "fail";
        }finally{
        	if(p!=null){
        		 p.close();
        	}
        }

        //create the sql to set auto-increment value
        log.info("Creating the auto-increment.sql in the root directory...");
        try{
            p = new PrintStream(new FileOutputStream(bimeConfiguration.getLabshome()+"/" + uniqueContextRoot+"/auto_increment.sql"));
            p.println(this.createAutoIncrementResetScript(labId, uniqueContextRoot));
        }catch (Exception e){
            log.error("file",e);
            return "fail";
	    }finally{
	    	if(p!=null){
	    		 p.close();
	    	}
	    }
	    //create the initialize_lab.sql to add the administrator to user
	    log.info("creating the initialize_lab.sql in root directory...");
	    try{
            p = new PrintStream(new FileOutputStream(bimeConfiguration.getLabshome()+"/" + uniqueContextRoot+"/initialize_lab.sql"));
            p.println(this.createLabInitialValueScript(labId, uniqueContextRoot, adminName, adminEmail, encrypedAdminPassword));
        }catch (Exception e){
            log.error("file",e);
            return "fail";
	    }finally{
	    	if(p!=null){
	    		 p.close();
	    	}
	    }
	    //create the drop-database.sql
	    log.info("creating the createDatabase.sql ....");
	    try{
            p = new PrintStream(new FileOutputStream(bimeConfiguration.getLabshome()+"/" + uniqueContextRoot+"/createDatabase.sql"));
            p.println(this.createCreateDatabaseScript(labId, uniqueContextRoot, adminPassword));
        }catch (Exception e){
            log.error("file",e);
            return "fail";
	    }finally{
	    	if(p!=null){
	    		 p.close();
	    	}
	    }
	  //create the drop-database.sql
	    log.info("creating dropDatabase.sql...");
	    try{
            p = new PrintStream(new FileOutputStream(bimeConfiguration.getLabshome()+"/" + uniqueContextRoot+"/dropDatabase.sql"));
            p.println(this.createDropDatabaseScript(labId, uniqueContextRoot));
        }catch (Exception e){
            log.error("file",e);
            return "fail";
	    }finally{
	    	if(p!=null){
	    		 p.close();
	    	}
	    }
	    
	    log.info("running the addLab.sh to execute the sql scripts to prepare the mysql database for client...");
        try {
        	//need to use super account to run the script, "tomcat" might not have the permission to run mysql
			returnCode = this.runShellscript("sudo " + bimeConfiguration.getBimehome()+"/scripts/client-management/addLab.sh " + uniqueContextRoot,bimeConfiguration.getLabshome());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        if(returnCode!=1){
			return "fail";
		}
        log.info("done creating root directory, sql scripts and mysql database for client:" + uniqueContextRoot);
        
		return "good";
	}
	
	private String createCreateDatabaseScript(Long labId, String labDatabaseName, String adminPassword){
		Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("contextRoot", labDatabaseName);
        model.put("adminPass", adminPassword);
		try{
            return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                                                         "createDatabase.vm", model);
		}catch(Exception e){
        	log.error("error",e);
        	throw new RuntimeException(e);
        }
	}
	
	private String createDropDatabaseScript(Long labId, String labDatabaseName){
		Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("contextRoot", labDatabaseName);
		try{
            return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                                                         "dropDatabase.vm", model);
		}catch(Exception e){
        	log.error("error",e);
        	throw new RuntimeException(e);
        }
	}
	
	private String createLabInitialValueScript(Long labId, String labDatabaseName, String adminName, String adminEmail, String adminPassword) {
		//use dddd
		//INSERT INTO `app_user` (id,created_date,last_update_date,version,account_expired,account_locked,cellPhoneNumber,credentials_expired,email,account_enabled,first_name,homePhoneNumber,last_name,password,password_hint,startDate,superUserId,title,username,workPhoneNumber) VALUES (1,null,null,0,0,0,null,0,'admin@bime.com',1,'admin',null,'lab','d033e22ae348aeb5660fc2140aec35850c4da997','standard',null,null,'administrator of lab','admin1',null);
		//INSERT INTO `user_role` (user_id,role_id) VALUES (1,'ROLE_ADMIN');
		BigInteger autoIncrementValue = new BigInteger("1000000").multiply(new BigInteger(labId.toString()));
		StringBuffer buf = new StringBuffer("\n" + "use " + labDatabaseName + ";");
		buf.append("\n" + "INSERT INTO `app_user` (created_date,last_update_date,version,account_expired,account_locked,cellPhoneNumber,credentials_expired,email,account_enabled,first_name,homePhoneNumber,last_name,password,password_hint,startDate,superUserId,title,username,workPhoneNumber) VALUES (null,null,0,0,0,null,0,'" + adminEmail +"',1,'" + adminName +"',null,'','" + adminPassword + "','standard',null,null,'administrator of lab','admin',null);");
		buf.append("\n" + "INSERT INTO `user_role` (user_id,role_id) VALUES (" + autoIncrementValue.toString() + ",'ROLE_ADMIN');");
		buf.append("\n" + "INSERT INTO `lab` (name,storageIdentity) VALUES('" + labDatabaseName + "','" + labDatabaseName+ ".bimelab.com');");
		return buf.toString();
	}

	private String createAutoIncrementResetScript(Long labId, String labDatabaseName){

		BigInteger autoIncrementValue = new BigInteger("1000000").multiply(new BigInteger(labId.toString()));
		//with root user
		//select table_name from information_schema.columns where table_schema = 'bime' and column_name='id'
		//use client_schema
		//alter table AUTO_INCREMENT = crId*1000000
		StringBuffer buf = new StringBuffer("\n" + "use " +labDatabaseName + ";");
		List<String> tableNames = this.labDao.findAllTablesWithIdColumn();
		for(String tableName:tableNames){
//			String tableName = (String)tableMap.get("table_name");
			buf.append("\n" + "alter table " + tableName + " AUTO_INCREMENT = " + autoIncrementValue.toString() + ";");
		}
		return buf.toString();
		
	}
	private int runShellscript(String command,String workingDirectory) throws IOException, InterruptedException{

		final Process process = Runtime.getRuntime().exec(command, null, new File(workingDirectory));
		 
        new Thread(new PipeInputStreamToOutputStreamRunnable(process.getErrorStream(), System.err)).start();
        new Thread(new PipeInputStreamToOutputStreamRunnable(process.getInputStream(), System.out)).start();
 
        final int returnCode = process.waitFor();
       log.info("Return code " + returnCode);
        return returnCode;

	}
}
