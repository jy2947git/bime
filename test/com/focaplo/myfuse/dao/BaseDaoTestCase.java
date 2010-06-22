package com.focaplo.myfuse.dao;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Base class for running DAO tests.
 * @author mraible
 */

public abstract class BaseDaoTestCase {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(BaseDaoTestCase.class);
    /**
     * ResourceBundle loaded from src/test/resources/${package.name}/ClassName.properties (if exists)
     */
    protected ResourceBundle rb;

    ApplicationContext applicationContext;
    
    SessionFactory sessionFactory;
    IUniversalDao universalDao;
    IUserDao userDao;
    IInventoryDao inventoryDao;
    IGrantDao grantDao;
    IProjectDao projectDao;
    ILabDao labDao;
    @BeforeClass
    public static void beforeClass(){

    }
    
    @AfterClass
    public static void afterClass(){
    	
    }
    @Before
    public void setUp() throws Exception {
    	log.info("loading spring...");
    	applicationContext = new ClassPathXmlApplicationContext("classpath*:applicationContext*.xml");
    	universalDao = (IUniversalDao)this.getApplicationContext().getBean("universalDao");
    	userDao = (IUserDao) this.getApplicationContext().getBean("userDao");
    	inventoryDao=(IInventoryDao) this.getApplicationContext().getBean("inventoryDao");
    	projectDao=(IProjectDao)this.applicationContext.getBean("projectDao");
    	grantDao=(IGrantDao)this.applicationContext.getBean("grantDao");
    	labDao=(ILabDao)this.applicationContext.getBean("labDao");
    	log.info("setting transaction...");
//		super.setUp();
		sessionFactory = (SessionFactory) this.applicationContext.getBean("sessionFactory");
		Session session = SessionFactoryUtils.getSession(sessionFactory, true);
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
	}

	@After
	public void tearDown() throws Exception {
//		super.tearDown();
		SessionHolder sessionHolder = (SessionHolder)TransactionSynchronizationManager.getResource(sessionFactory);
		Session session = sessionHolder.getSession();
		session.flush();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.releaseSession(session, sessionFactory);
	}

	/**
     * Sets AutowireMode to AUTOWIRE_BY_NAME and configures all context files needed to tests DAOs.
     * @return String array of Spring context files.
     */
//    protected String[] getConfigLocations() {
//        setAutowireMode(AUTOWIRE_BY_NAME);
//        return new String[] {
//                "classpath:/applicationContext-resources.xml",
//                "classpath:/applicationContext-dao.xml",
//                "classpath*:/applicationContext.xml", // for modular projects
//                "classpath:**/applicationContext*.xml" // for web projects
//            };
//    }

    public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
     * Default constructor - populates "rb" variable if properties file exists for the class in
     * src/test/resources.
     */
    public BaseDaoTestCase() {
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
//        String className = this.getClass().getName();
//
//        try {
//            rb = ResourceBundle.getBundle(className);
//        } catch (MissingResourceException mre) {
//            //log.warn("No resource bundle found for: " + className);
//        }
    }

    /**
     * Utility method to populate a javabean-style object with values
     * from a Properties file
     * @param obj the model object to populate
     * @return Object populated object
     * @throws Exception if BeanUtils fails to copy properly
     */
//    protected Object populate(Object obj) throws Exception {
//        // loop through all the beans methods and set its properties from its .properties file
//        Map<String, String> map = new HashMap<String, String>();
//
//        for (Enumeration<String> keys = rb.getKeys(); keys.hasMoreElements();) {
//            String key = keys.nextElement();
//            map.put(key, rb.getString(key));
//        }
//
//        BeanUtils.copyProperties(map, obj);
//
//        return obj;
//    }

    /**
     * Create a HibernateTemplate from the SessionFactory and call flush() and clear() on it.
     * Designed to be used after "save" methods in tests: http://issues.appfuse.org/browse/APF-178.
     */
//    protected void flush() {
//        HibernateTemplate hibernateTemplate =
//                new HibernateTemplate((SessionFactory) applicationContext.getBean("sessionFactory"));
//        hibernateTemplate.flush();
//        hibernateTemplate.clear();
//    }
}
