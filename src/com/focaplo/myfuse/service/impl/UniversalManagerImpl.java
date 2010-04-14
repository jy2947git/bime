package com.focaplo.myfuse.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.focaplo.myfuse.dao.GrantDao;
import com.focaplo.myfuse.dao.InventoryDao;
import com.focaplo.myfuse.dao.LabDao;
import com.focaplo.myfuse.dao.OrderDao;
import com.focaplo.myfuse.dao.ProjectDao;
import com.focaplo.myfuse.dao.UniversalDao;
import com.focaplo.myfuse.dao.UserDao;
import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.service.UniversalManager;

/**
 * Base class for Business Services - use this class for utility methods and
 * generic CRUD methods.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UniversalManagerImpl implements UniversalManager {
    /**
     * Log instance for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * UniversalDao instance, ready to charge forward and persist to the database
     */
    protected UniversalDao dao;
    @Autowired
    protected UserDao userDao;
    @Autowired
    protected InventoryDao inventoryDao;
    @Autowired
    protected ProjectDao projectDao;
    @Autowired
    protected GrantDao grantDao;
    @Autowired
    protected OrderDao orderDao;
    @Autowired
    protected LabDao labDao;
    
    public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setProjectDao(ProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public void setGrantDao(GrantDao grantDao) {
		this.grantDao = grantDao;
	}

	public void setDao(UniversalDao dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    public Object get(Class clazz, Serializable id) {
        return dao.get(clazz, id);
    }

    /**
     * {@inheritDoc}
     */
    public List getAll(Class clazz) {
        return dao.getAll(clazz);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(Class clazz, Serializable id) {
        dao.remove(clazz, id);
    }

    /**
     * {@inheritDoc}
     */
    public Object save(BaseObject o) {
        return dao.save(o);
    }

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public InventoryDao getInventoryDao() {
		return inventoryDao;
	}

	public void setInventoryDao(InventoryDao inventoryDao) {
		this.inventoryDao = inventoryDao;
	}

	public UniversalDao getDao() {
		return dao;
	}

	public void deleteEntities(Class clazz, List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.dao.remove(clazz, id);
		}
	}

	public void setLabDao(LabDao labDao) {
		this.labDao = labDao;
	}
    
    
}
