package com.focaplo.myfuse.model;

/**
 * In our data model, the storage and storage section has 1 to N relationship, storage is one level
 * above section. However, the user can choose to store an item in either storage level or section level.
 * To help this feature, we have the storage and section both implement the same interface.
 *
 */
public interface Storagible {

	public String getAlias();
	public Long getId();
	public Class getStorageType();
	public String getStorigibleUniqueId();
}
