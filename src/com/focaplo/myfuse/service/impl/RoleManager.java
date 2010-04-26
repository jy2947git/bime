package com.focaplo.myfuse.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.focaplo.myfuse.dao.IRoleDao;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.service.RoleService;

/**
 * Implementation of RoleManager interface.
 * 
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 */
@Service(value="roleManager")
public class RoleManager extends UniversalManager implements RoleService {
    private IRoleDao dao;

    public void setRoleDao(IRoleDao dao) {
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    public List<Role> getRoles(Role role) {
        return dao.getAllRoles();
    }

    /**
     * {@inheritDoc}
     */
    public Role getRole(String rolename) {
        return dao.getRoleByName(rolename);
    }

    /**
     * {@inheritDoc}
     */
    public Role saveRole(Role role) {
        return dao.saveRole(role);
    }

    /**
     * {@inheritDoc}
     */
    public void removeRole(String rolename) {
        dao.removeRole(rolename);
    }
}