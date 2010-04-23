package com.focaplo.myfuse.service.impl;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.focaplo.myfuse.dao.ILabDao;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.model.LabMeetingItem;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.StorageService;

public class LabManager extends UniversalManager implements LabService {
	@Autowired
	private ILabDao labDao;
	@Autowired
	private StorageService storageService;
	
	public void setStorageService(StorageService storageService) {
		this.storageService = storageService;
	}

	public void setLabDao(ILabDao labDao) {
		this.labDao = labDao;
	}

	public void saveMeeting(LabMeeting meeting) {
		this.dao.saveOrUpdate(meeting);
		Set<LabMeetingItem> items = meeting.getMeetingItems();
		for(LabMeetingItem item:items){
			this.dao.saveOrUpdate(item);
		}
	}

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

	public void removeLab(Long id) {
		Lab lab = (Lab) this.labDao.get(Lab.class, id);

		this.storageService.removeStorageForLab(lab);

		this.labDao.remove(Lab.class, id);
	}

	public void setupStorageForLab(Lab lab) {
		this.storageService.prepareStorageForLab(lab);

		this.labDao.saveOrUpdate(lab);
	}
	
	public void saveLab(Lab lab){
		this.labDao.saveOrUpdate(lab);
	}

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
}
