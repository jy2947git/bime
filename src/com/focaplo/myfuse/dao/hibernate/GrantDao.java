package com.focaplo.myfuse.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.focaplo.myfuse.dao.IGrantDao;
@Repository(value="grantDao")
public class GrantDao extends UniversalDao implements
		IGrantDao {

}
