package com.focaplo.myfuse.dao.hibernate;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import com.focaplo.myfuse.dao.IUniversalDao;
import com.focaplo.myfuse.model.BaseObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

/**
 * This class serves as the a class that can CRUD any object witout any
 * Spring configuration. The only downside is it does require casting
 * from Object to the object class.
 *
 * @author Bryan Noll
 */
@Repository(value="universalDao")
public class UniversalDao implements IUniversalDao {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    @Autowired
    SessionFactory sessionFactory;
    
    public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
     * {@inheritDoc}
     */
    public Object save(BaseObject o) {
    	log.debug("save " + o);
    	if(o.getCreatedDate()==null){
    		o.setCreatedDate(new Date());
    	}
    	o.setLastUpdateDate(new Date());
        this.getSessionFactory().getCurrentSession().saveOrUpdate(o);
        return o;
    }

    public Object saveOrUpdate(BaseObject o){
    	log.debug("save or update " + o);
    	if(o.getCreatedDate()==null){
    		o.setCreatedDate(new Date());
    	}
    	o.setLastUpdateDate(new Date());
    	this.getSessionFactory().getCurrentSession().saveOrUpdate(o);
    	
    	return o;
    }
    /**
     * {@inheritDoc}
     */
    public Object get(Class clazz, Serializable id) {
        Object o = this.getSessionFactory().getCurrentSession().get(clazz, id);

        if (o == null) {
            throw new ObjectRetrievalFailureException(clazz, id);
        }

        return o;
    }

    /**
     * {@inheritDoc}
     */
    public List getAll(Class clazz) {
        return this.getSessionFactory().getCurrentSession().createCriteria(clazz).list();
    }

    /**
     * {@inheritDoc}
     */
    public void remove(Class clazz, Serializable id) {
    	this.getSessionFactory().getCurrentSession().delete(get(clazz, id));
//        this.getHibernateTemplate().flush();
    }
}
