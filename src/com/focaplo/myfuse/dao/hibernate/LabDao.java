package com.focaplo.myfuse.dao.hibernate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.focaplo.myfuse.dao.ILabDao;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.LabMeeting;
import com.focaplo.myfuse.model.LabMeetingItem;
import com.focaplo.myfuse.model.MeetingFile;
import com.focaplo.myfuse.model.ResourceUserAuthorization;
import com.focaplo.myfuse.model.User;

public class LabDao extends UniversalDao implements ILabDao{

	@SuppressWarnings("unchecked")
	public List<LabMeetingItem> getMeetingItemsOfMeeting(Long meetingId) {
		return this.getSessionFactory().getCurrentSession().createQuery("from LabMeetingItem where meeting.id=?").setLong(0, meetingId).list();
	}

	public List<MeetingFile> getMeetingFilesOfMeeting(Long meetingId) {
		return this.getSessionFactory().getCurrentSession().createQuery("from MeetingFile where meeting.id=?").setLong(0, meetingId).list();
	}

	public Lab getLabByname(String labName) {
		return (Lab) this.getSessionFactory().getCurrentSession().createQuery("from Lab where name=?").setString(0, labName).uniqueResult();
	}

	public void removeLabByName(String labName) {
		this.getSessionFactory().getCurrentSession().createQuery("delete from Lab where name=?").setString(0, labName).executeUpdate();
	}

	public List<ResourceUserAuthorization> loodLabResourcePermission() {
		return this.getSessionFactory().getCurrentSession().createQuery("from ResourceUserAuthorization").list();
	}

	public ResourceUserAuthorization getResourceUserAuthorizationByUserAndResource(
			String resourceType, Long resourceId, String userName) {
		return (ResourceUserAuthorization) this.getSessionFactory().getCurrentSession().createQuery("from ResourceUserAuthorization where resourceType=? and resourceId=? and userName=?").setString(0, resourceType).setLong(1, resourceId).setString(2, userName).uniqueResult();
	}

	public List<LabMeeting> getMeetingsParticipatedByUser(Long id) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.DAY_OF_YEAR, today.get(Calendar.DAY_OF_YEAR)-7);
		today.set(Calendar.HOUR_OF_DAY, 0);
		List<LabMeeting> results = this.getSessionFactory().getCurrentSession().createQuery("select m from LabMeeting m join m.participants mp where mp=? and m.startCalendar>=?").setLong(0, id).setDate(1, today.getTime()).list();
//		List<LabMeeting> results = new ArrayList<LabMeeting>();
//		// TODO Auto-generated method stub
//		User user = (User)this.get(User.class, id);
//		Set<LabMeeting> meetings = user.getMeetings();
//		results.addAll(meetings);
		return results;
	}

}
