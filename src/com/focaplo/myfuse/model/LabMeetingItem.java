package com.focaplo.myfuse.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="lab_meetin_item")
public class LabMeetingItem extends BaseObject implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column
	private int itemOrder;
	@OneToOne(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JoinColumn
	private User speaker;
	@Column
	private String topic;
	@ManyToOne
	@JoinColumn(name="lab_meeting_id")
	private LabMeeting meeting;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(int itemOrder) {
		this.itemOrder = itemOrder;
	}



	public User getSpeaker() {
		return speaker;
	}

	public void setSpeaker(User speaker) {
		this.speaker = speaker;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public LabMeeting getMeeting() {
		return meeting;
	}

	public void setMeeting(LabMeeting meeting) {
		this.meeting = meeting;
	}

}
