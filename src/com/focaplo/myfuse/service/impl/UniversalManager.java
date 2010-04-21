package com.focaplo.myfuse.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.focaplo.myfuse.dao.IGrantDao;
import com.focaplo.myfuse.dao.IInventoryDao;
import com.focaplo.myfuse.dao.ILabDao;
import com.focaplo.myfuse.dao.IOrderDao;
import com.focaplo.myfuse.dao.IProjectDao;
import com.focaplo.myfuse.dao.IUniversalDao;
import com.focaplo.myfuse.dao.IUserDao;
import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.service.UniversalService;

/**
 * Base class for Business Services - use this class for utility methods and
 * generic CRUD methods.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UniversalManager implements UniversalService {
    /**
     * Log instance for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * UniversalDao instance, ready to charge forward and persist to the database
     */
    protected IUniversalDao dao;
    @Autowired
    protected IUserDao userDao;
    @Autowired
    protected IInventoryDao inventoryDao;
    @Autowired
    protected IProjectDao projectDao;
    @Autowired
    protected IGrantDao grantDao;
    @Autowired
    protected IOrderDao orderDao;
    @Autowired
    protected ILabDao labDao;
    
    public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setProjectDao(IProjectDao projectDao) {
		this.projectDao = projectDao;
	}

	public void setGrantDao(IGrantDao grantDao) {
		this.grantDao = grantDao;
	}

	public void setDao(IUniversalDao dao) {
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

	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	public IInventoryDao getInventoryDao() {
		return inventoryDao;
	}

	public void setInventoryDao(IInventoryDao inventoryDao) {
		this.inventoryDao = inventoryDao;
	}

	public IUniversalDao getDao() {
		return dao;
	}

	public void deleteEntities(Class clazz, List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.dao.remove(clazz, id);
		}
	}

	public void setLabDao(ILabDao labDao) {
		this.labDao = labDao;
	}
    
    
}
