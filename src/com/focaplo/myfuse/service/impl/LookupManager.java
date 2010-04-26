package com.focaplo.myfuse.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.focaplo.myfuse.dao.ILookupDao;
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.service.LookupService;


/**
 * Implementation of LookupManager interface to talk to the persistence layer.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Service(value="lookupManager")
public class LookupManager extends UniversalManager implements LookupService {
	@Autowired
    private ILookupDao lookupDao;

  

    public void setLookupDao(ILookupDao lookupDao) {
		this.lookupDao = lookupDao;
	}



	/**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRoles() {
        List<Role> roles = this.lookupDao.getRoles();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (Role role1 : roles) {
            list.add(new LabelValue(role1.getId(), role1.getId()));
        }

        return list;
    }
}
