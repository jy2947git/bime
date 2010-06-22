package com.focaplo.myfuse.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Transactional(readOnly=true)
public class InventoryManager extends UniversalManager implements
		InventoryService {
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public Long saveRefrigerator(Refrigerator r) {
		
		return this.saveStorage(r);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
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

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public Long saveStorage(Storage s) {
		Storage toSave = (Storage) this.inventoryDao.save(s);
//		the section alias is populated at web layer since it needs locale message
//		Iterator<StorageSection> ite = toSave.getSections().iterator();
//		while(ite.hasNext()){
//			StorageSection ss = ite.next();
//			ss.setStorage(toSave);
//			ss.setAlias(toSave.getAlias() + " " + "section" + " " + ss.getName());
//			this.save(ss);
//		}
		return toSave.getId();
	}

	public List<Storage> searchStorage() {
		return this.inventoryDao.getAll(Storage.class);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteEquipments(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.inventoryDao.remove(Equipment.class, id);
		}
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteItems(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.inventoryDao.remove(ManagedItem.class, id);
		}
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteStorages(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.deleteStorage(id);
		}
	}

	public List<StorageSection> getSectionsOfStorage(Long id) {
		return this.inventoryDao.getSectionsOfStorage(id);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteStorageSection(Long id) {
		//this will delete the storage section, also, will find the items which were stored in ths section
		// and set its storagible unique id to be NULL
		StorageSection ss = (StorageSection)this.inventoryDao.get(StorageSection.class, id);
		String storagibleId = ss.getStorigibleUniqueId();
		//update the items
		this.inventoryDao.removeItemsOutOfStoragible(storagibleId);
		this.inventoryDao.deleteStorageSection(id);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void updateStorageSection(Long id, String name, String type) {
		this.inventoryDao.updateStorageSectionAttributes(id, name, type);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void saveNewStorageSection(Long storageId, StorageSection section) {
		if(section.getId()!=null){
			log.warn("id should be null");
			section.setId(null);
		}
		if(storageId==null){
			log.error("storage can not be null and storage id can not be null " + section.getStorage());
			return;
		}
		section.setStorage((Storage) this.inventoryDao.get(Storage.class, storageId));
		section.setAlias(section.getStorage().getAlias() + " " + "section" + " " + section.getName());
		this.inventoryDao.save(section);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public List<ManagedItem> addOrderToInventory(Long orderId) {
		//find order items
		List<ManagedItem> results = new ArrayList<ManagedItem>();
		List<OrderItem> items = this.orderDao.getOrderItemsOfOrder(orderId);
		for(OrderItem item:items){
			results.add(this.addToInventory(item));
		}
		return results;
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public ManagedItem addToInventory(OrderItem orderItem) {
		ManagedItem item = new ManagedItem();
		item.setItemCategory(orderItem.getItemCategory());
		item.setAmount(orderItem.getAmount());
		item.setOrderItem(orderItem);
		this.updateItemCategtoryAmount(orderItem.getItemCategory(), orderItem.getAmount());
		this.inventoryDao.saveNewInventoryItem(item);
		return item;
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void updateItemCategtoryAmount(ItemCategory itemCategory,
			Integer amount) {
		itemCategory.setTotalAmount(new Integer(itemCategory.getTotalAmount().intValue() + amount.intValue()));
		this.inventoryDao.save(itemCategory);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void addOrderItemToInventory(Long orderItemId) {
		OrderItem orderItem = (OrderItem) this.orderDao.get(OrderItem.class, orderItemId);
		this.addToInventory(orderItem);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteItemCategories(List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.inventoryDao.remove(ItemCategory.class, id);
		}
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void addNotesToItem(ManagedItem item,String notes){
		InventoryAudit audit = new InventoryAudit();
		audit.setMessage(notes);
		audit.setManagedItem(item);
		this.inventoryDao.save(audit);
	}
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
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
