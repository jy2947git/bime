package com.focaplo.myfuse.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.focaplo.myfuse.dao.IInventoryDao;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.Refrigerator;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;
@Repository(value="inventoryDao")
public class InventoryDao extends UniversalDao implements IInventoryDao {

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
		this.remove(StorageSection.class, storageSectionId);
	}

	public void deleteStorge(Long storageId) {
			this.remove(Storage.class, storageId);
	}

	public void removeItemsOutOfStoragible(String storigibleUniqueId) {
		//update all the items to set NULL
		this.getSessionFactory().getCurrentSession().createQuery("update ManagedItem set storigibleUniqueId=null where storigibleUniqueId=?").setString(0, storigibleUniqueId).executeUpdate();

		
	}

}
