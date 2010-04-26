package com.focaplo.myfuse.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.InventoryAudit;
import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.OrderItem;
import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.model.Storagible;
import com.focaplo.myfuse.service.InventoryService;

@Service(value="inventoryManager")
public class InventoryManager extends UniversalManager implements
		InventoryService {

	public Long saveRefrigerator(Refrigerator r) {
		
		return this.saveStorage(r);
	}

	public void deleteStorage(Long id) {
		//this will delete the storage and its sections
		//also this will set the items which were associated to the storage or section to be NULL
		Storage s = (Storage)this.inventoryDao.get(Storage.class, id);
		Set<StorageSection> allSections = s.getSections();
		this.inventoryDao.removeItemsOutOfStoragible(s.getStorigibleUniqueId());
		for(StorageSection ss:allSections){
			this.inventoryDao.removeItemsOutOfStoragible(ss.getStorigibleUniqueId());
		}
		this.inventoryDao.deleteStorge(id);
		return;
	}

	public Long saveStorage(Storage s) {
		return this.inventoryDao.saveStorage(s);
	}

	public List<Storage> searchStorage() {
		return this.inventoryDao.getAll(Storage.class);
	}

	public void deleteEquipments(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.inventoryDao.remove(Equipment.class, id);
		}
	}

	public void deleteItems(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.inventoryDao.remove(ManagedItem.class, id);
		}
	}

	public void deleteStorages(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.deleteStorage(id);
		}
	}

	public List<StorageSection> getSectionsOfStorage(Long id) {
		return this.inventoryDao.getSectionsOfStorage(id);
	}

	public void deleteStorageSection(Long id) {
		//this will delete the storage section, also, will find the items which were stored in ths section
		// and set its storagible unique id to be NULL
		StorageSection ss = (StorageSection)this.inventoryDao.get(StorageSection.class, id);
		String storagibleId = ss.getStorigibleUniqueId();
		//update the items
		this.inventoryDao.removeItemsOutOfStoragible(storagibleId);
		this.inventoryDao.deleteStorageSection(id);
	}

	public void updateStorageSection(Long id, String name, String type) {
		this.inventoryDao.updateStorageSectionAttributes(id, name, type);
	}

	public void saveStorageSection(Long storageId, StorageSection section) {
		if(section.getId()!=null){
			log.warn("id should be null");
			section.setId(null);
		}
		if(storageId==null){
			log.error("storage can not be null and storage id can not be null " + section.getStorage());
			return;
		}
		section.setStorage((Storage) this.inventoryDao.get(Storage.class, storageId));
		//TODO - how to get the locale from application resources just like the form controller?
		//this method is called directly by DWR Ajax...
		section.setAlias(section.getStorage().getAlias() + " " + "section" + " " + section.getName());
		this.inventoryDao.save(section);
	}


	public List<ManagedItem> addOrderToInventory(Long orderId) {
		//find order items
		List<ManagedItem> results = new ArrayList<ManagedItem>();
		List<OrderItem> items = this.orderDao.getOrderItemsOfOrder(orderId);
		for(OrderItem item:items){
			results.add(this.addToInventory(item));
		}
		return results;
	}

	public ManagedItem addToInventory(OrderItem orderItem) {
		ManagedItem item = new ManagedItem();
		item.setItemCategory(orderItem.getItemCategory());
		item.setAmount(orderItem.getAmount());
		item.setOrderItem(orderItem);
		this.updateItemCategtoryAmount(orderItem.getItemCategory(), orderItem.getAmount());
		this.inventoryDao.saveNewInventoryItem(item);
		return item;
	}

	public void updateItemCategtoryAmount(ItemCategory itemCategory,
			Integer amount) {
		itemCategory.setTotalAmount(new Integer(itemCategory.getTotalAmount().intValue() + amount.intValue()));
		this.inventoryDao.save(itemCategory);
	}

	public void addOrderItemToInventory(Long orderItemId) {
		OrderItem orderItem = (OrderItem) this.orderDao.get(OrderItem.class, orderItemId);
		this.addToInventory(orderItem);
	}

	public void deleteItemCategories(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.inventoryDao.remove(ItemCategory.class, id);
		}
	}

	public void addNotesToItem(ManagedItem item,String notes){
		InventoryAudit audit = new InventoryAudit();
		audit.setMessage(notes);
		audit.setManagedItem(item);
		this.inventoryDao.save(audit);
	}

	public void updateAndCheckInventoryJob() {
		//
		log.info("checking inventory....");
	}

	public List<Storagible> getAllStoragibles() {
		List<Storage> storages = this.getAll(Storage.class);
		List<StorageSection> sections = this.getAll(StorageSection.class);
		
		//combine the storage and storage sections since user can choose to either store at storage level or section level
		List<Storagible> allStoragibles = new ArrayList<Storagible>();
		allStoragibles.addAll(storages);
		allStoragibles.addAll(sections);
		//sort by alias
		Collections.sort(allStoragibles, new Comparator<Storagible>(){

			public int compare(Storagible o1, Storagible o2) {
				if(o1==null){
					return -1;
				}
				return o1.getAlias().compareTo(o2.getAlias());
			}});
		return allStoragibles;
	}

}
