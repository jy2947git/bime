package com.focaplo.myfuse.service.impl;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
@Service(value="universalManager")
@Transactional(readOnly=true)
public class UniversalManager implements UniversalService {
    /**
     * Log instance for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * UniversalDao instance, ready to charge forward and persist to the database
     */
    @Autowired
    @Qualifier("universalDao")
    protected IUniversalDao universalDao;
    @Autowired
    @Qualifier("userDao")
    protected IUserDao userDao;
    @Autowired
    @Qualifier("inventoryDao")
    protected IInventoryDao inventoryDao;
    @Autowired
    @Qualifier("projectDao")
    protected IProjectDao projectDao;
    @Autowired
    @Qualifier("grantDao")
    protected IGrantDao grantDao;
    @Autowired
    @Qualifier("orderDao")
    protected IOrderDao orderDao;
    @Autowired
    @Qualifier("labDao")
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

	

    public List getAll(Class clazz) {
        return universalDao.getAll(clazz);
    }

    @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
    public void remove(Class clazz, Serializable id) {
        universalDao.remove(clazz, id);
    }

    @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
    public Object save(BaseObject o) {
        return universalDao.save(o);
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

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteEntities(Class clazz, List<Long> toBeDeleted) {
		for(Long id:toBeDeleted){
			this.universalDao.remove(clazz, id);
		}
	}

	public void setLabDao(ILabDao labDao) {
		this.labDao = labDao;
	}

	public Object get(Class clazz, Serializable id) {
		return universalDao.get(clazz, id);
	}
    
    
}
