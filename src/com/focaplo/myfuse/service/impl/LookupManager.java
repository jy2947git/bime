package com.focaplo.myfuse.service.impl;

import com.focaplo.myfuse.dao.ILookupDao;
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.service.LookupService;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of LookupManager interface to talk to the persistence layer.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class LookupManager extends UniversalManager implements LookupService {
    private ILookupDao dao;

    /**
     * Method that allows setting the DAO to talk to the data store with.
     * @param dao the dao implementation
     */
    public void setLookupDao(ILookupDao dao) {
        super.dao = dao;
        this.dao = dao;
    }

    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRoles() {
        List<Role> roles = dao.getRoles();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (Role role1 : roles) {
            list.add(new LabelValue(role1.getId(), role1.getId()));
        }

        return list;
    }
}
