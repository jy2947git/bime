package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name="lab_meeting")
public class LabMeeting extends BaseObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column
	private Calendar startCalendar = Calendar.getInstance();
	@Column
	private Calendar endCalendar = Calendar.getInstance();
	
	@OneToOne(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	@JoinColumn
	private User coordinator;
	@Column(length=50)
	private String subject;
	
	@OneToMany(mappedBy="meeting", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<MeetingFile> meetingFiles=new HashSet<MeetingFile>();
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(length=2000)
	private String message;
	@OneToMany(mappedBy="meeting", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<LabMeetingItem> meetingItems = new HashSet<LabMeetingItem>();
	@ManyToMany(targetEntity=User.class,fetch=FetchType.EAGER)
	@JoinTable(name="meeting_participants",joinColumns=@JoinColumn(name="meeting_id"),inverseJoinColumns=@JoinColumn(name="user_id"))
	private Set<User> participants = new HashSet<User>();
	
	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}
	@Transient
	public List<LabelValue> getParticipantsLabelList(){
		List<LabelValue> accessUserLabels = new ArrayList<LabelValue>();
		for(User user:this.participants){
			accessUserLabels.add(new LabelValue(user.getFullName(), user.getId().toString()));
		}
		return accessUserLabels;
	}

	@Transient
	public String getStartHourAndMinute(){
		return this.startCalendar.get(Calendar.HOUR_OF_DAY) + ":"+this.startCalendar.get(Calendar.MINUTE);
	}
	@Transient
	public String getEndHourAndMinute(){
		return this.endCalendar.get(Calendar.HOUR_OF_DAY) + ":"+this.endCalendar.get(Calendar.MINUTE);
	}
	@Transient
	public void setStartHourAndMinute(String hourAndMinute){
		//hour:min
		String[] segs = hourAndMinute.split(":");
		this.startCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(segs[0]));
		this.startCalendar.set(Calendar.MINUTE, Integer.parseInt(segs[1]));
	}
	@Transient
	public void setEndHourAndMinute(String hourAndMinute){
		//hour:min
		String[] segs = hourAndMinute.split(":");
		this.endCalendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(segs[0]));
		this.endCalendar.set(Calendar.MINUTE, Integer.parseInt(segs[1]));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}





	@Transient
	public Date getStartDate() {
		return startCalendar.getTime();
	}
	@Transient
	public void setStartDate(Date startDate) {
		this.startCalendar.setTime(startDate);
	}
	@Transient
	public Date getEndDate() {
		return endCalendar.getTime();
	}
	@Transient
	public void setEndDate(Date endDate) {
		this.endCalendar.setTime(endDate);
	}

	public User getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(User coordinator) {
		this.coordinator = coordinator;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Set<LabMeetingItem> getMeetingItems() {
		return meetingItems;
	}

	public void setMeetingItems(Set<LabMeetingItem> meetingItems) {
		this.meetingItems = meetingItems;
	}

	public Set<MeetingFile> getMeetingFiles() {
		return meetingFiles;
	}

	public void setMeetingFiles(Set<MeetingFile> meetingFiles) {
		this.meetingFiles = meetingFiles;
	}

}
