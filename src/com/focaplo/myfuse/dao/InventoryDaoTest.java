package com.focaplo.myfuse.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.focaplo.myfuse.model.Equipment;
import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;

public class InventoryDaoTest extends BaseDaoTestCase {
	
	
	public void setInventoryDao(InventoryDao inventoryDao) {
		this.inventoryDao = inventoryDao;
	}
	@Test
	public void testAddNewRefrigeratorManully(){
		Refrigerator r = new Refrigerator();
		r.setType("refrigerator_n_4");
		r.setName("refrigerator" + System.currentTimeMillis());
		
		StorageSection ss = new StorageSection();
		ss.setName("ss");
		StorageSection ss2 = new StorageSection();
		ss2.setName("ss2");
		r.getSections().add(ss);
		r.getSections().add(ss2);
		ss.setStorage(r);
		ss2.setStorage(r);
		this.inventoryDao.save(r);
		log.info("refrigerator id" + r.getId());
		this.inventoryDao.save(ss);
		this.inventoryDao.save(ss2);
//		this.setComplete();
	}
	@Test
	public void testAddNewStorage(){
		Storage r = new Storage();
		r.setType("storage_other");
		r.setName("storage" + System.currentTimeMillis());
		StorageSection ss = new StorageSection();
		ss.setName("ss");
		StorageSection ss2 = new StorageSection();
		ss2.setName("ss2");
		r.getSections().add(ss);
		r.getSections().add(ss2);
		this.inventoryDao.saveStorage(r);
		log.info("storage id" + r.getId());

	}
	@Test
	public void testUpdateStorageSection(){
		StorageSection ss = (StorageSection) this.inventoryDao.get(StorageSection.class, new Long("1"));
		ss.setName("changed");
		this.inventoryDao.saveOrUpdate(ss);
	}

	
	@Test
	public void testUpdateStorage(){
		Storage s = (Storage)this.universalDao.get(Storage.class, new Long("2"));
		s.setName("chanegd");
		this.universalDao.saveOrUpdate(s);
	}
	
	@Test
	public void testSaveNewInventoryItemManual(){
		ItemCategory category = new ItemCategory();
		category.setName("stuff");
		category.setName("whatever");
		this.inventoryDao.save(category);
		ManagedItem item = new ManagedItem();
		item.setAmount(new Integer("100"));
		item.setMaker("Ford");
		item.setItemCategory(category);
		item.setStorePersonId("1");
		
		//storage
		List<Storage> slist = this.inventoryDao.getAll(Storage.class);
		int i=0;
		while(i<slist.size() && slist.get(i).getSections().size()<=0){
			i++;
		}
		if(i==slist.size()){
			log.info("no storage has section...");
		}else{
			item.setStorageSection(slist.get(i).getSections().iterator().next());
			log.info("storage section:" + item.getStorageSection().getStorage().getId() + "-" + item.getStorageSection().getId());
		}
		this.inventoryDao.save(item);
		category.setTotalAmount(new Integer("100"));
		this.inventoryDao.save(category);
	}
	
	@Test
	public void testFindItemsOfStorageSection(){
		List<Storage> slist = this.inventoryDao.getAll(Storage.class);
		int i=0;
		while(i<slist.size() && slist.get(i).getSections().size()<=0){
			i++;
		}
		if(i==slist.size()){
			log.info("no storage has section...");
		}else{
			StorageSection ss = slist.get(i).getSections().iterator().next();
			Iterator<ManagedItem> ite = ss.getManagedItems().iterator();
			while(ite.hasNext()){
				log.info("found:" + ite.next().getId());
			}
		}
	}
	
	@Test
	public void testFindItemsOfStorage(){
		List<Storage> slist = this.inventoryDao.getAll(Storage.class);
		int i=0;
		while(i<slist.size() && slist.get(i).getSections().size()<=0){
			i++;
		}
		if(i==slist.size()){
			log.info("no storage has section...");
		}else{
			Iterator<StorageSection> its = slist.get(i).getSections().iterator();
				while(its.hasNext()){
				StorageSection ss = its.next();
				Iterator<ManagedItem> ite = ss.getManagedItems().iterator();
				while(ite.hasNext()){
					log.info("found:" + ite.next().getId());
				}
			}
		}
	}
	
	@Test
	public void testMoveItemFromOneSectionToTheOther(){
		ManagedItem item = (ManagedItem)this.inventoryDao.get(ManagedItem.class, new Long("13"));
		item.setAmount(item.getAmount()-10);
		log.info("item used to be at:" + item.getStorageSection().getId());
		//find a storage section
		List<Storage> slist = this.inventoryDao.getAll(Storage.class);
		int i=0;
		while(i<slist.size() && slist.get(i).getSections().size()<=0){
			i++;
		}
		if(i==slist.size()){
			log.info("no storage has section...");
		}else{
			
			Iterator<StorageSection> its = slist.get(i).getSections().iterator();
			while(its.hasNext()){
				StorageSection ss = its.next();
				if(ss.getId().longValue()!=item.getStorageSection().getId().longValue()){
					log.info("move to " + ss.getStorage().getId() + "-" + ss.getId());
					item.setStorageSection(ss);
					break;
				}
			}
		}
		this.inventoryDao.save(item);
	}
	
	@Test
	public void testAddNewEquipment(){
		Equipment e = new Equipment();
		e.setContactUserName("admin");
		e.setEquiptmentCondition("good");
		e.setLastUserName("admin");
		e.setLocation("corner");
		e.setName("bike2");
		e.setType("sports");
		this.universalDao.save(e);
		log.info("saved:"+e.getId());
	}
	
	@Test
	public void testGetSectionsOfStorage(){
		List sections = this.inventoryDao.getSectionsOfStorage(new Long("3"));
		log.info("find " + sections.size());
	}
	
	@Test
	public void testUpdateStorageSectionAttributes(){
		this.inventoryDao.updateStorageSectionAttributes(new Long("3"), "updated name", "new type");
	}
	@Test
	public void testRemoveStorageSection(){
		this.inventoryDao.deleteStorageSection(new Long("10"));
	}
	
	@Test
	public void testDeleteStorage(){
		this.inventoryDao.deleteStorge(new Long("3"));
	}
}
