package com.focaplo.myfuse.service.impl;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.focaplo.myfuse.dao.IProjectDao;
import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.ExperimentNote;
import com.focaplo.myfuse.model.ExperimentProtocol;
import com.focaplo.myfuse.model.ExperimentProtocolAudit;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.ManagedProject;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.ToDo;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.model.WorkLog;
import com.focaplo.myfuse.service.AuthorizationService;
import com.focaplo.myfuse.service.ProjectService;
import com.focaplo.myfuse.service.StorageService;

@Service(value="projectManager")
@Transactional(readOnly=true)
public class ProjectManager extends UniversalManager implements
		ProjectService {
	@Autowired
	private IProjectDao projectDao;
	@Autowired
	@Qualifier("storageManager")
	private StorageService storageService;
	@Autowired
	private AuthorizationService authorizationManager;
	
	public void setAuthorizationManager(AuthorizationService authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	public void setProjectDao(IProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public StorageService getStorageService() {
		return storageService;
	}

	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteProjects(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.projectDao.remove(ManagedProject.class, id);
		}
	}

	public List<ManagedProject> getProjectAccessibleTo(Long userId) {
		List<ManagedProject> projects = this.projectDao.getProjectParticipatedBy(userId);
		//sort
		return projects;
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void saveExperimentNote(ExperimentNote note) {
		log.info("find " + note.getAccessedBy().size());
		note.getAccessedBy().add(note.getResearcher());
		this.projectDao.saveOrUpdate(note);
    	//set the permission. The researcher have delete, and authorize
    	//the access uers have edit
		Set<User> owners = new HashSet<User>();
		owners.add(note.getResearcher());
    	this.authorizationManager.updatePermissionToResource(ExperimentNote.class, note.getId(), Securable.authorize, owners);
    	this.authorizationManager.updatePermissionToResource(ExperimentNote.class, note.getId(), Securable.delete, owners);
    	
    	this.authorizationManager.updatePermissionToResource(ExperimentNote.class, note.getId(), Securable.edit, note.getAccessedBy());
//		Set<ExperimentImage> images = note.getExperimentImages();
//		for(ExperimentImage image:images){
//			this.projectDao.saveOrUpdate(image);
//		}
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void saveProtocol(ExperimentProtocol protocol, boolean realDirty, String dirtyUser) {
		if(protocol.getId()!=null && realDirty){
			//create a new protocol object
			ExperimentProtocol ep = new ExperimentProtocol();
			ep.setCreatedByName(dirtyUser);
			ep.setExperimentProcedure(protocol.getExperimentProcedure());
			ep.setName(protocol.getName());
			ep.setProtocolVersion(protocol.getProtocolVersion()+1);
			this.projectDao.save(ep);
			ExperimentProtocolAudit audit = new ExperimentProtocolAudit();
			audit.setExperimentProtocol(ep);
			audit.setMessage("new protocol version created by " + dirtyUser + " on " + new Date());
			this.projectDao.save(audit);
			
		}else{
			this.projectDao.saveOrUpdate(protocol);
			ExperimentProtocolAudit audit = new ExperimentProtocolAudit();
			audit.setExperimentProtocol(protocol);
			audit.setMessage("Protocol updated by " + dirtyUser + " on " + new Date());
			this.projectDao.save(audit);
			Set<ExperimentNote> notes = protocol.getExperimentNotes();
			for(ExperimentNote note:notes){
				this.saveExperimentNote(note);
			}
			
		}
		
		
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteProtocols(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.projectDao.remove(ExperimentProtocol.class, id);
		}
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteNotes(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.projectDao.remove(ExperimentNote.class, id);
		}
	}

	

	public List<ExperimentNote> getNotesOfProject(Long projectId) {
		return this.projectDao.getNotesOfProject(projectId);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteToDos(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.projectDao.remove(ToDo.class, id);
		}
	}

	public List<ToDo> getToDoOfProject(Long projectId) {
		return this.projectDao.getToDosOfProject(projectId);
	}

	public List<WorkLog> getWorkLogsOfToDo(Long toDoId) {
		return this.projectDao.getWorkLogsOfToDo(toDoId);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void uploadExperimentImageFile(Lab lab, ExperimentImage imageFile,
			ByteArrayInputStream stream) {
		//first save the meeting file
		if(imageFile.getId()!=null){
			throw new RuntimeException("imageFile file has to be new object for uploading");
		}
		this.projectDao.save(imageFile);
		//now call the storage manager to upload file, which will also update the meeting file with real storage identifier
		this.storageService.storeFile(lab, imageFile, stream);
		//now update the meeting file
		log.info("after storing file, the storage identifier is " + imageFile.getUniqueStorageIdentifier());
		this.projectDao.saveOrUpdate(imageFile);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void removeExperimentImageFile(Lab lab, ExperimentImage imageFile){
		if(imageFile.getId()==null){
			throw new RuntimeException("imageFile file has to be existing object for deleting");
		}
		if(imageFile.getUniqueStorageIdentifier()!=null){
			this.storageService.removeFile(lab, imageFile);
		}
		this.projectDao.remove(ExperimentImage.class, imageFile.getId());
	}

	public List<ExperimentImage> getExperimentImagesOfNote(Long noteId) {
		return this.projectDao.getExperimentImagesOfNote(noteId);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void saveProject(ManagedProject project) {
		
		boolean newProject=false;
		if(project.getId()==null){
			newProject=true;
		}
		this.save(project);
		
			this.authorizationManager.turnOnPermission(ManagedProject.class, project.getId(), Securable.edit, project.getPrincipal().getUsername());
			
		
		//refresh the PI authorize
		this.authorizationManager.updatePermissionToResource(ManagedProject.class, project.getId(), Securable.authorize, project.getOwners());
		this.authorizationManager.updatePermissionToResource(ManagedProject.class, project.getId(), Securable.delete, project.getOwners());
		//refresh participants
		
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void saveProjectParticipants(ManagedProject project) {
		//save to participants table
		this.save(project);
		//delete all
		this.authorizationManager.updatePermissionToResource(ManagedProject.class, project.getId(), Securable.edit, project.getParticipants());

	}
}
