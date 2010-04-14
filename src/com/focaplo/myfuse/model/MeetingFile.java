package com.focaplo.myfuse.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
@Entity
@Table(name="meeting_file")
@PrimaryKeyJoinColumn(name="stored_file_id")
public class MeetingFile extends StoredFile {
	@ManyToOne
	@JoinColumn(name="lab_meeting_id")
	private LabMeeting meeting;
		


	public MeetingFile() {
		super();
		this.setFileType(StoredFile.MEETING_FILE);
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public LabMeeting getMeeting() {
		return meeting;
	}

	public void setMeeting(LabMeeting meeting) {
		this.meeting = meeting;
	}


}
