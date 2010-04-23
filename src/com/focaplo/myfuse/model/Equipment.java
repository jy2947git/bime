package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="equiptment")
public class Equipment extends BaseObject implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	@Column(nullable=false,length=50,unique=false)
	private String name;
	@Column(nullable=false,length=50)
	private String type;
	@Column(nullable=true,length=50)
	private String equiptmentCondition;

	@Column(nullable=true,length=50)
	private String location;
	@OneToOne(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JoinColumn
	private User lastUser;
	@OneToOne(cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
	@JoinColumn
	private User contactPerson;
	@Column
	private Date lastMaintainenceDate;
	@Column
	private Date nextMaintanenceDate;
	@Column(length=200)
	private String maintainenceTools;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



	public String getEquiptmentCondition() {
		return equiptmentCondition;
	}

	public void setEquiptmentCondition(String equiptmentCondition) {
		this.equiptmentCondition = equiptmentCondition;
	}

	

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public User getLastUser() {
		return lastUser;
	}

	public void setLastUser(User lastUser) {
		this.lastUser = lastUser;
	}

	public User getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(User contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Date getLastMaintainenceDate() {
		return lastMaintainenceDate;
	}

	public void setLastMaintainenceDate(Date lastMaintainenceDate) {
		this.lastMaintainenceDate = lastMaintainenceDate;
	}

	public Date getNextMaintanenceDate() {
		return nextMaintanenceDate;
	}

	public void setNextMaintanenceDate(Date nextMaintanenceDate) {
		this.nextMaintanenceDate = nextMaintanenceDate;
	}

	public String getMaintainenceTools() {
		return maintainenceTools;
	}

	public void setMaintainenceTools(String maintainenceTools) {
		this.maintainenceTools = maintainenceTools;
	}


	
	
}
