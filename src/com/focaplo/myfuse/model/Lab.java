package com.focaplo.myfuse.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name="lab")
public class Lab extends BaseObject implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(nullable=false)
	private String name;

	@Basic
	private String storageIdentity; //to identify the lab's storage root, for example, in S3, it will be
	//the bucket name like "lab123.bimelab.com", in local mode, it is the lab name like "lab123"
	@Basic
	private String password;
	
	@Override
	public boolean equals(Object o) {
		if(this==o){
			return true;
		}
		if(!(o instanceof Lab)){
			return false;
		}
		final Lab l = (Lab)o;
	
		return this.name.equalsIgnoreCase(l.getName());
	}

	@Override
	public int hashCode() {
		return name==null?0:name.hashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder tb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("name", this.name);
		return tb.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public String getStorageIdentity() {
		return storageIdentity;
	}

	public void setStorageIdentity(String storageIdentity) {
		this.storageIdentity = storageIdentity;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



}
