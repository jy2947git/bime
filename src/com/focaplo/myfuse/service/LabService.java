package com.focaplo.myfuse.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import com.focaplo.myfuse.model.ExperimentImage;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.model.LabMeetingItem;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.model.User;

public interface LabService extends UniversalService {

	public void saveMeeting(LabMeeting meeting);

	public List<User> getLabMembers();

	public List<LabMeetingItem> getMeetingItemsOfMeeting(Long meetingId);

	public List<MeetingFile> getMeetingFilesOfMeeting(Long meetingId);
	
//	public void updateMeetingItems(LabMeeting meeting);
	
	public void saveLab(Lab lab);
	
	public void setupStorageForLab(Lab lab);
	
	public void removeLab(Long id);
	
	public void uploadMeetingFile(Lab lab, MeetingFile meetingFile, ByteArrayInputStream is);

	public Lab getLabByName(String labName);

	public void removeLabByName(String labName);
	
	public void removeMeetingFile(Lab lab, MeetingFile meetingFile);

	public List<LabMeeting> getMeetingsInviting(Long id);


	public String createNewLab(Long labId, String uniqueContextRoot, String labName,String adminName, String adminEmail, String adminPassword) throws Exception;

}
