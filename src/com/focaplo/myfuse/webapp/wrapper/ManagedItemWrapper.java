package com.focaplo.myfuse.webapp.wrapper;

import java.io.Serializable;

import com.focaplo.myfuse.model.ManagedItem;

public class ManagedItemWrapper implements Serializable {
	private ManagedItem managedItem;
	private Long storageSectionId;
	private Long storageId;
	private Long itemCategoryId;
	private Integer inventoryAmount;
	
	public Integer getInventoryAmount() {
		return inventoryAmount;
	}
	public void setInventoryAmount(Integer inventoryAmount) {
		this.inventoryAmount = inventoryAmount;
	}
	public ManagedItem getManagedItem() {
		return managedItem;
	}
	public void setManagedItem(ManagedItem managedItem) {
		this.managedItem = managedItem;
	}
	public Long getStorageSectionId() {
		return storageSectionId;
	}
	public void setStorageSectionId(Long storageSectionId) {
		this.storageSectionId = storageSectionId;
	}
	public Long getStorageId() {
		return storageId;
	}
	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}
	public Long getItemCategoryId() {
		return itemCategoryId;
	}
	public void setItemCategoryId(Long itemCategoryId) {
		this.itemCategoryId = itemCategoryId;
	}
	
}
