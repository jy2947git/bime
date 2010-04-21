package com.focaplo.myfuse.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.focaplo.myfuse.service.impl.AmazonStorageManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext*.xml")
@TransactionConfiguration
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public abstract class BaseManagerTestCase{
    //~ Static fields/initializers =============================================

    protected final Log log = LogFactory.getLog(getClass());
//    ApplicationContext applicationContext;
    @Autowired
    UserService userManager;
    @Autowired
    InventoryService inventoryManager;
    @Autowired
    RoleService roleManager;
    @Autowired
    ProjectService projectManager;
    @Autowired
    OrderService orderManager;
    @Autowired
    StorageService storageService;
    @Autowired
    LabService labManager;
    @Autowired
    EncryptionService encrypter;
    
//    public void setUp() throws Exception {
//    	log.info("loading spring...");
//    	applicationContext = new ClassPathXmlApplicationContext("classpath*:applicationContext*.xml");
//    	userManager = (UserManager) this.applicationContext.getBean("userManager");
//    	inventoryManager = (InventoryManager) this.applicationContext.getBean("inventoryManager");
//    	roleManager = (RoleManager)this.applicationContext.getBean("roleManager");
//    }
    
//    protected static ResourceBundle rb = null;
//
//    protected String[] getConfigLocations() {
//        setAutowireMode(AUTOWIRE_BY_NAME);
//        return new String[] {"/applicationContext-resources.xml", "classpath:/applicationContext-dao.xml",
//                             "/applicationContext-service.xml", "classpath*:/**/applicationContext.xml"};
//        // classpath*:/**/applicationContext.xml has to be used since this file does not
//        // exist in AppFuse, but may exist in projects that depend on it
//    }
//
//    //~ Constructors ===========================================================
//
//    public BaseManagerTestCase() {
//        // Since a ResourceBundle is not required for each class, just
//        // do a simple check to see if one exists
//        String className = this.getClass().getName();
//
//        try {
//            rb = ResourceBundle.getBundle(className);
//        } catch (MissingResourceException mre) {
//            //log.warn("No resource bundle found for: " + className);
//        }
//    }
//
//    //~ Methods ================================================================
//
//    /**
//     * Utility method to populate a javabean-style object with values
//     * from a Properties file
//     *
//     * @param obj the model object to populate
//     * @return Object populated object
//     * @throws Exception if BeanUtils fails to copy properly
//     */
//    protected Object populate(Object obj) throws Exception {
//        // loop through all the beans methods and set its properties from
//        // its .properties file
//        Map map = ConvertUtil.convertBundleToMap(rb);
//
//        BeanUtils.copyProperties(obj, map);
//
//        return obj;
//    }
    
}