package com.focaplo.myfuse.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext*.xml")
@TransactionConfiguration(defaultRollback=false)
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public abstract class BaseManagerTestCase{
    //~ Static fields/initializers =============================================

    protected final Log log = LogFactory.getLog(getClass());
//    ApplicationContext applicationContext;
    @Autowired
    protected UniversalService universalManager;
    @Autowired
    protected UserService userManager;
    @Autowired
    protected InventoryService inventoryManager;
    @Autowired
    protected RoleService roleManager;
    @Autowired
    protected ProjectService projectManager;
    @Autowired
    protected OrderService orderManager;
    @Autowired
    @Qualifier("storageManager")
    protected StorageService storageService;
    @Autowired
    protected LabService labManager;
    @Autowired
    protected EncryptionService encrypter;
	@Autowired
	DataSourceService dynDataSourceRegister;
    @Autowired
	protected ApplicationContext context;
	@Autowired
	CacheService memcachedManager;
}