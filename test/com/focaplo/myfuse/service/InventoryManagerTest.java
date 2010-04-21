package com.focaplo.myfuse.service;

import org.junit.Test;

import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.StorageSection;

public class InventoryManagerTest extends BaseManagerTestCase {

	@Test
	public void testAddNewRefrigerator(){
		Refrigerator r = new Refrigerator();
		r.setType("refrigerator_n_4");
		r.setName("refrigerator" + System.currentTimeMillis());
		log.info("refrigerator id" + r.getId());
//		this.setComplete();
		StorageSection ss = new StorageSection();
		StorageSection ss2 = new StorageSection();
		r.getSections().add(ss);
		r.getSections().add(ss2);
//		InventoryManager manager = (InventoryManager)this.applicationContext.getBean("inventoryManager");
		inventoryManager.saveRefrigerator(r);
//		this.setComplete();
		log.info("saved " + r.getId());
	}
	@Test
	public void testChangeItemSection(){
		ManagedItem mi = (ManagedItem) this.inventoryManager.get(ManagedItem.class, new Long("13"));
		this.inventoryManager.saveManagedItemToStorageSection(mi, new Long("14"));
		
	}
}
