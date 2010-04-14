package com.focaplo.myfuse.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="equiptment")
public class Equipment extends BaseObject implements Serializable {

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
		return null;
	}
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	@Column(nullable=false,length=50,unique=false)
	private String name;
	@Column(nullable=false,length=50)
	private String type;
	@Column(nullable=true,length=50)
	private String equiptmentCondition;
	@Column(nullable=true,length=50)
	private String contactUserName;
	@Column(nullable=true,length=50)
	private String location;
	@Column(nullable=true,length=50)
	private String lastUserName;
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

	public String getContactUserName() {
		return contactUserName;
	}

	public void setContactUserName(String contactUserName) {
		this.contactUserName = contactUserName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLastUserName() {
		return lastUserName;
	}

	public void setLastUserName(String lastUserName) {
		this.lastUserName = lastUserName;
	}
	
	
	
}
