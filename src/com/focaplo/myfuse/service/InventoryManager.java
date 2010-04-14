package com.focaplo.myfuse.service;

import java.util.List;

import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.ManagedOrder;
import com.focaplo.myfuse.model.OrderItem;
import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;

public interface InventoryManager extends UniversalManager{

	public Long saveRefrigerator(Refrigerator r);
	public Long saveStorage(Storage s);
	public void deleteStorage(Long id);
	public List<Storage> searchStorage();
	public void deleteEquipments(List<Long> toBeDeleted);
	public void deleteItems(List<Long> toBeDeleted);
	public void deleteStorages(List<Long> toBeDeleted);
	public List<StorageSection> getSectionsOfStorage(Long id);
	public void updateStorageSection(Long id, String name, String type);
	public void deleteStorageSection(Long id);
	public void saveStorageSection(Long storageId, StorageSection section);
	public ManagedItem saveManagedItemToStorageSection(ManagedItem item, Long storageSectionId);
	public List<ManagedItem> addOrderToInventory(Long orderId);
	public void addOrderItemToInventory(Long orderItemId);
	public ManagedItem addToInventory(OrderItem item);
	public void updateItemCategtoryAmount(ItemCategory itemCategory,
			Integer amount);
	public void deleteItemCategories(List<Long> toBeDeleted);
	public void addNotesToItem(ManagedItem item,String notes);
	
	//
	public void updateAndCheckInventoryJob();
}
