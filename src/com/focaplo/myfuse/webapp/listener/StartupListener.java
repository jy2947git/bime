package com.focaplo.myfuse.webapp.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.service.AuthorizationService;
import com.focaplo.myfuse.service.LookupService;
import com.focaplo.myfuse.service.impl.MemcachedManager;

/**
 * <p>StartupListener class used to initialize and database settings
 * and populate any application-wide drop-downs.
 * <p/>
 */
public class StartupListener implements ServletContextListener {
    private static final Log log = LogFactory.getLog(StartupListener.class);

    @SuppressWarnings({"unchecked"})
    public void contextInitialized(ServletContextEvent event) {
        log.debug("Initializing context...");

        ServletContext context = event.getServletContext();

        // Orion starts Servlets before Listeners, so check if the config
        // object already exists
        Map<String, Object> config = (HashMap<String, Object>) context.getAttribute(Constants.CONFIG);

        if (config == null) {
            config = new HashMap<String, Object>();
        }
        
        if (context.getInitParameter(Constants.CSS_THEME) != null) {
            config.put(Constants.CSS_THEME, context.getInitParameter(Constants.CSS_THEME));
        }

        ApplicationContext ctx =
            WebApplicationContextUtils.getRequiredWebApplicationContext(context);

        /*String[] beans = ctx.getBeanDefinitionNames();
        for (String bean : beans) {
            log.debug(bean);
        }*/
        
        PasswordEncoder passwordEncoder = null;
        try {
            ProviderManager provider = (ProviderManager) ctx.getBean("_authenticationManager");
            for (Object o : provider.getProviders()) {
                AuthenticationProvider p = (AuthenticationProvider) o;
                if (p instanceof RememberMeAuthenticationProvider) {
                    config.put("rememberMeEnabled", Boolean.TRUE);
                } else if (ctx.getBean("passwordEncoder") != null) {
                    passwordEncoder = (PasswordEncoder) ctx.getBean("passwordEncoder");
                }
            }
        } catch (NoSuchBeanDefinitionException n) {
            log.debug("authenticationManager bean not found, assuming test and ignoring...");
            // ignore, should only happen when testing
        }

        context.setAttribute(Constants.CONFIG, config);

        setupContext(context);

    }

    /**
     * This method uses the LookupManager to lookup available roles from the data layer.
     * @param context The servlet context
     */
    public static void setupContext(ServletContext context) {
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);

        //initialize the authorization service to load the whole table
        AuthorizationService am = (AuthorizationService)ctx.getBean("authorizationManager");
        am.refresh();
    }

    /**
     * Shutdown servlet context (currently a no-op method).
     *
     * @param servletContextEvent The servlet context event
     */
    public void contextDestroyed(ServletContextEvent event) {
        //LogFactory.release(Thread.currentThread().getContextClassLoader());
        //Commented out the above call to avoid warning when SLF4J in classpath.
        //WARN: The method class org.apache.commons.logging.impl.SLF4JLogFactory#release() was invoked.
        //WARN: Please see http://www.slf4j.org/codes.html for an explanation.
    	 ServletContext context = event.getServletContext();
    	 ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
    	 log.info("trying to shutdown memcached clients...");
    	 MemcachedManager cacheManager= ctx.getBean(MemcachedManager.class);
    	 if(cacheManager!=null){
    		 cacheManager.turnOff();
    	 }
    }
}
