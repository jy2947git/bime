package com.focaplo.myfuse.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
@Entity
@Table(name="meeting_file")
@PrimaryKeyJoinColumn(name="stored_file_id")
public class MeetingFile extends StoredFile {
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="lab_meeting_id")
	private LabMeeting meeting;
		


	public MeetingFile() {
		super();
		this.setFileType(StoredFile.MEETING_FILE);
	}

	public LabMeeting getMeeting() {
		return meeting;
	}

	public void setMeeting(LabMeeting meeting) {
		this.meeting = meeting;
	}


}
