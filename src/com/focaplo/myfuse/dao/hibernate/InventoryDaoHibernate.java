package com.focaplo.myfuse.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import com.focaplo.myfuse.dao.InventoryDao;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;

public class InventoryDaoHibernate extends UniversalDaoHibernate implements InventoryDao {

	public Long saveRefrigerator(Refrigerator r) {
		return this.saveStorage(r);
	}

	public Long saveStorage(Storage r) {
		this.save(r);
		Iterator<StorageSection> ite = r.getSections().iterator();
		while(ite.hasNext()){
			StorageSection ss = ite.next();
			ss.setStorage(r);
			this.save(ss);
		}
		return r.getId();
	}

	public Long saveNewInventoryItem(ManagedItem item) {
		this.save(item);
		return item.getId();
	}

	@SuppressWarnings("unchecked")
	public List<StorageSection> getSectionsOfStorage(Long id) {
		return this.getSessionFactory().getCurrentSession().createQuery("from StorageSection where storage.id=?").setLong(0, id).list();
	}

	public void updateStorageSectionAttributes(Long id, String name, String type) {
		this.getSessionFactory().getCurrentSession().createQuery("update StorageSection set name=?, type=? where id=?").setString(0, name).setString(1, type).setLong(2,id).executeUpdate();
	}

	public void deleteStorageSection(Long storageSectionId) {
		//update all the items to set NULL
		this.getSessionFactory().getCurrentSession().createQuery("update ManagedItem set storageSection=null where storageSection=?").setLong(0, storageSectionId).executeUpdate();
		this.remove(StorageSection.class, storageSectionId);
	}

	public void deleteStorge(Long storageId) {
		//update all the items to set NULL
		this.getSessionFactory().getCurrentSession().createQuery("update ManagedItem set storageSection=null where storageSection in (from StorageSection where storage=?)").setLong(0, storageId).executeUpdate();
		this.remove(Storage.class, storageId);
	}

}
