package com.focaplo.myfuse.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="storage")
public class Storage extends BaseObject implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	@Column(nullable=false,length=50,unique=false)
	private String name;
	@Column(nullable=false,length=50)
	private String type;
	@OneToMany(mappedBy="storage",cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	private Set<StorageSection> sections = new HashSet<StorageSection>();
	@Column(nullable=true,length=50)
	private String location;
	@Column(nullable=true,length=50)
	private String contactPersion;
	
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

	public Set<StorageSection> getSections() {
		return sections;
	}

	public void setSections(Set<StorageSection> sections) {
		this.sections = sections;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContactPersion() {
		return contactPersion;
	}

	public void setContactPersion(String contactPersion) {
		this.contactPersion = contactPersion;
	}

}
