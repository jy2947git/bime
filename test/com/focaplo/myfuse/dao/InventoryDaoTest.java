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
	
	
	public void setInventoryDao(IInventoryDao inventoryDao) {
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
		if(!slist.isEmpty()){
			item.setStorigibleUniqueId(slist.get(0).getStorigibleUniqueId());
		}
		this.inventoryDao.save(item);
		category.setTotalAmount(new Integer("100"));
		this.inventoryDao.save(category);
	}
	
	@Test
	public void testFindItemsOfStorageSection(){
		
	}
	
	@Test
	public void testFindItemsOfStorage(){
		
	}
	
	
	
	@Test
	public void testAddNewEquipment(){
		Equipment e = new Equipment();
		
		e.setEquiptmentCondition("good");
	
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
