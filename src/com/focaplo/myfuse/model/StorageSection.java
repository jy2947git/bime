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
public class StorageSection extends BaseObject implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	@Column(nullable=true,length=50)
	private String name;
	@Column(nullable=true,length=50)
	private String type;
	@ManyToOne
	@JoinColumn(name="storage_id")
	private Storage storage;
	@OneToMany(mappedBy="storageSection", fetch=FetchType.LAZY)
	private Set<ManagedItem> managedItems = new HashSet<ManagedItem>();
	@Transient
	public String getFullName(){
		return this.getStorage().getName() + "-" + this.getName();
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
		return null;
	}

	public Storage getStorage() {
		return storage;
	}

	public void setStorage(Storage storage) {
		this.storage = storage;
	}

	public Set<ManagedItem> getManagedItems() {
		return managedItems;
	}

	public void setManagedItems(Set<ManagedItem> managedItems) {
		this.managedItems = managedItems;
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

}
