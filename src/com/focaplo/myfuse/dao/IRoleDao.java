package com.focaplo.myfuse.dao;

import java.util.List;

import com.focaplo.myfuse.model.Role;

/**
 * Role Data Access Object (DAO) interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public interface IRoleDao extends IUniversalDao {
    /**
     * Gets role information based on rolename
     * @param rolename the rolename
     * @return populated role object
     */
    Role getRoleByName(String rolename);

    /**
     * Removes a role from the database by name
     * @param rolename the role's rolename
     */
    void removeRole(String rolename);

	List<Role> getAllRoles();

	Role saveRole(Role role);
}
