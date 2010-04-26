package com.focaplo.myfuse.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.focaplo.myfuse.dao.IRoleDao;
import com.focaplo.myfuse.model.Role;


/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve Role objects.
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> 
 */
@Repository(value="roleDao")
public class RoleDao extends UniversalDao implements IRoleDao {


    /**
     * {@inheritDoc}
     */
    public Role getRoleByName(String rolename) {
        List<Role> roles = this.getSessionFactory().getCurrentSession().createQuery("from Role where id=?").setString(0, rolename).list();
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
        this.remove(Role.class, rolename);
    }

	public List<Role> getAllRoles() {
		return this.getAll(Role.class);
	}

	public Role saveRole(Role role) {
		this.getSessionFactory().getCurrentSession().saveOrUpdate(role);
		return role;
	}
}
