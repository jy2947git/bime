package com.focaplo.myfuse.dao.hibernate;

import java.util.List;

import com.focaplo.myfuse.dao.ILookupDao;
import com.focaplo.myfuse.model.Role;

/**
 * Hibernate implementation of LookupDao.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupDao extends UniversalDao implements ILookupDao {

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<Role> getRoles() {
        log.debug("Retrieving all role names...");
        return this.getSessionFactory().getCurrentSession().createQuery("from Role order by id").list();
    }
}