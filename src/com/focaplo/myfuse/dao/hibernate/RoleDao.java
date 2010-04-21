package com.focaplo.myfuse.dao.hibernate;

import java.util.List;

import com.focaplo.myfuse.dao.IRoleDao;
import com.focaplo.myfuse.model.Role;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Role objects.
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> 
 */
public class RoleDao extends GenericDao<Role, Long> implements IRoleDao {

    /**
     * Constructor to create a Generics-based version using Role as the entity
     */
    public RoleDao() {
        super(Role.class);
    }

    /**
     * {@inheritDoc}
     */
    public Role getRoleByName(String rolename) {
        List roles = getHibernateTemplate().find("from Role where id=?", rolename);
        if (roles.isEmpty()) {
            return null;
        } else {
            return (Role) roles.get(0);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void removeRole(String rolename) {
        Object role = getRoleByName(rolename);
        getHibernateTemplate().delete(role);
    }
}
