package com.focaplo.myfuse.dao;

import java.util.List;

import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;

public interface InventoryDao extends UniversalDao{

	public Long saveStorage(Storage s);
	public Long saveRefrigerator(Refrigerator r);
	public Long saveNewInventoryItem(ManagedItem item);
	public List<StorageSection> getSectionsOfStorage(Long id);
	public void updateStorageSectionAttributes(Long id, String name, String type);
	public void deleteStorge(Long storageId);
	public void deleteStorageSection(Long storeageSectionId);
}
