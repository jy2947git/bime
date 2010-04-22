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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="storage_section")
public class StorageSection extends BaseObject implements Serializable, Storagible {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	@Column(nullable=true,length=50)
	private String name;
	@Column(nullable=true,length=50)
	private String type;
	@ManyToOne
	@JoinColumn(name="storage_id")
	private Storage storage;

	@Column(nullable=false,length=100)
	private String alias;
	
	@Transient
	public String getFullName(){
		return this.getStorage().getName() + "-" + this.getName();
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Class getStorageType() {
		return StorageSection.class;
	}
	public String getStorigibleUniqueId() {
		return this.getStorageType().getSimpleName() + "-" + this.getId();
	}
}
