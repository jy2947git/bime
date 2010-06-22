package com.focaplo.myfuse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.focaplo.myfuse.dao.IRoleDao;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.service.RoleService;

/**
 * Implementation of RoleManager interface.
 * 
 * @author <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 */
@Service(value="roleManager")
@Transactional(readOnly=true)
public class RoleManager extends UniversalManager implements RoleService {
	@Autowired
    private IRoleDao roleDao;



    public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}


    public List<Role> getRoles(Role role) {
        return roleDao.getAllRoles();
    }

    public Role getRole(String rolename) {
        return roleDao.getRoleByName(rolename);
    }

    @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
    public Role saveRole(Role role) {
        return roleDao.saveRole(role);
    }

    @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
    public void removeRole(String rolename) {
        roleDao.removeRole(rolename);
    }
}