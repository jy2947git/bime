package com.focaplo.myfuse.dao;

import java.util.List;

import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.model.LabMeetingItem;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.model.ResourceUserAuthorization;

public interface ILabDao extends IUniversalDao {

	List<LabMeetingItem> getMeetingItemsOfMeeting(Long meetingId);

	List<MeetingFile> getMeetingFilesOfMeeting(Long meetingId);

	Lab getLabByname(String labName);

	void removeLabByName(String labName);

	List<ResourceUserAuthorization> loodLabResourcePermission();

	ResourceUserAuthorization getResourceUserAuthorizationByUserAndResource(
			String resourceType, Long resourceId, String userName);

	List<LabMeeting> getMeetingsParticipatedByUser(Long id);
}
