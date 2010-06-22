package com.focaplo.myfuse.webapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.focaplo.myfuse.exception.UnauthorizedAccessException;
import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.ChemicalShelves;
import com.focaplo.myfuse.model.Lab;
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.Securable;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.AuthorizationService;
import com.focaplo.myfuse.service.CacheService;
import com.focaplo.myfuse.service.GrantService;
import com.focaplo.myfuse.service.InventoryService;
import com.focaplo.myfuse.service.LabService;
import com.focaplo.myfuse.service.OrderService;
import com.focaplo.myfuse.service.ProjectService;
import com.focaplo.myfuse.service.RoleService;
import com.focaplo.myfuse.service.StorageService;
import com.focaplo.myfuse.service.UniversalService;
import com.focaplo.myfuse.service.UserService;
import com.focaplo.myfuse.service.impl.MailEngine;
import com.focaplo.myfuse.webapp.spring.ThreadBoundContext;

@Controller
public class BimeFormController implements MessageSourceAware {
	protected final transient Log log = LogFactory.getLog(getClass());
	public static final String MESSAGES_KEY = "successMessages";
	@Autowired
	protected MessageSource messageSource;
	@Autowired
	protected AuthorizationService authorizationManager;
	@Autowired
	protected UserService userManager = null;
	@Autowired
	protected MailEngine mailEngine = null;
	@Autowired
	protected OrderService orderManager;

	@Autowired
	protected LabService labManager;

	@Autowired
	protected GrantService grantManager;
	@Autowired
	protected ProjectService projectManager;
	@Autowired
	protected InventoryService inventoryManager;
	@Autowired
	protected RoleService roleManager;
	@Autowired
	@Qualifier("storageManager")
	protected StorageService storageManager;
	@Autowired
	@Qualifier("memcachedManager")
	protected CacheService cacheManager;
	@Autowired
	protected UniversalService universalManager;
	
	public void setUniversalManager(UniversalService universalManager) {
		this.universalManager = universalManager;
	}
	public void setCacheManager(CacheService cacheManager) {
		this.cacheManager = cacheManager;
	}
	public void setStorageManager(StorageService storageManager) {
		this.storageManager = storageManager;
	}

	public void setRoleManager(RoleService roleManager) {
		this.roleManager = roleManager;
	}

	@SuppressWarnings("unchecked")
	public void saveError(HttpServletRequest request, String error) {
		List errors = (List) request.getSession().getAttribute("errors");
		if (errors == null) {
			errors = new ArrayList();
		}
		errors.add(error);
		request.getSession().setAttribute("errors", errors);
	}

	@SuppressWarnings("unchecked")
	public void saveMessage(HttpServletRequest request, String msg) {
		List messages = (List) request.getSession().getAttribute(MESSAGES_KEY);

		if (messages == null) {
			messages = new ArrayList();
		}

		messages.add(msg);
		request.getSession().setAttribute(MESSAGES_KEY, messages);
	}

	public String getText(String msgKey, Locale locale) {
		return messageSource.getMessage(msgKey, null, locale);
	}

	public String getText(String msgKey, String arg, Locale locale) {
		return messageSource.getMessage(msgKey, new Object[] { arg }, locale);
	}

	public String getText(String msgKey, Object[] args, Locale locale) {
		return messageSource.getMessage(msgKey, args, locale);
	}

	public void setMessageSource(MessageSource arg0) {
		this.messageSource = arg0;

	}

//	public abstract Class<? extends BaseObject> getModelClass();
	
	protected List getListOfModelObject(Class<? extends BaseObject> clazz){
		List objects = null;
		//use cache
		if(this.cacheManager!=null&&this.cacheManager.isOn()){
			objects  = (List<? extends BaseObject>) this.cacheManager.get(clazz.getSimpleName()+"-list");
		}
		if(objects==null){
			objects = this.universalManager.getAll(clazz);
			if(this.cacheManager!=null&&this.cacheManager.isOn()){
				this.cacheManager.set(clazz.getSimpleName()+"-list", CacheService.default_ttl_in_seconds, objects);
			}
		}
		return objects;
	}
	protected BaseObject getModelObject(Class<? extends BaseObject> clazz, Long id, boolean isCachedOk){
		BaseObject obj = null;
        if (id!=null && id.intValue()>0) {
        	if(this.cacheManager!=null&&this.cacheManager.isOn()){
        		obj = (BaseObject)this.cacheManager.get(clazz.getSimpleName()+"-"+id);
        	}
        	if(obj==null){
        		obj = (BaseObject) this.universalManager.get(clazz, id);
        		if(this.cacheManager!=null&&this.cacheManager.isOn()){
        			this.cacheManager.set(clazz.getSimpleName()+"-"+id, CacheService.default_ttl_in_seconds, obj);
        		}
        	}
        } else {
        	try {
				obj = (BaseObject) clazz.newInstance();
			} catch (Exception e) {
				log.error("error", e);
				throw new RuntimeException("Failed to create instance of class " + clazz);
			} 
        }
       return obj;
	}
	protected void expireCachedObjects(Class<? extends BaseObject> clazz, Long id) {
		//expire cache
		if(this.cacheManager!=null&&this.cacheManager.isOn()){
			this.cacheManager.delete(clazz.getSimpleName()+"-"+id);
			this.cacheManager.delete(clazz.getSimpleName()+"-list");
		}
		
	}
	
	public User getLoginUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

	protected void checkAccess(Class clazz, Long id, String permission)
			throws Exception {

		Class[] interfaces = clazz.getInterfaces();
		boolean secured = false;
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i].getName().equalsIgnoreCase(
					Securable.class.getName())) {
				// secured resource
				secured = true;
				break;
			}
		}
		if (secured) {

			if (id != null) {
				if (!this.authorizationManager.getHasPermission(clazz,
						new Long(id), this.getLoginUser().getUsername(),
						permission)) {
					throw new UnauthorizedAccessException("User "
							+ this.getLoginUser().getUsername()
							+ " tried to unauthorizaed " + permission
							+ " access to " + clazz + " id " + id);
				}
			} else {
				log.warn("Resource " + clazz
						+ " form controller doesn't have ID parameter!!!!");
			}

		}

	}

	protected String toJson(Object obj) throws JsonGenerationException,
			JsonMappingException, IOException {
		// JSON the list
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(obj);
		// System.out.println(json);
		return json;
	}

	public void setAuthorizationManager(
			AuthorizationService authorizationManager) {
		this.authorizationManager = authorizationManager;
	}

	public void setUserManager(UserService userManager) {
		this.userManager = userManager;
	}

	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	public void setGrantManager(GrantService grantManager) {
		this.grantManager = grantManager;
	}

	public void setProjectManager(ProjectService projectManager) {
		this.projectManager = projectManager;
	}

	public void setOrderManager(OrderService orderManager) {
		this.orderManager = orderManager;
	}

	public void setLabManager(LabService labManager) {
		this.labManager = labManager;
	}

	public void setInventoryManager(InventoryService inventoryManager) {
		this.inventoryManager = inventoryManager;
	}

	protected List getLabUsers() {
		return this.getListOfModelObject(User.class);
	}
	
	protected List<LabelValue> getLabelValueListOfUsers() {
		List<User> allUsers = this.getLabUsers();
		List<LabelValue> alllabels = new ArrayList<LabelValue>();
		for(User user:allUsers){
			alllabels.add(new LabelValue(user.getFullName(), user.getId().toString()));
		}
		return alllabels;
	}
	public Lab getCurrentLab() {
		String labName = ThreadBoundContext.getValue();
    	Lab lab = null;
    	if(this.cacheManager!=null&&this.cacheManager.isOn()){
    		lab = (Lab) this.cacheManager.get("lab-"+labName);
    	}
    	if(lab==null){
    		lab = this.labManager.getLabByName(labName);
    		if(this.cacheManager!=null&&this.cacheManager.isOn()){
    			this.cacheManager.set("lab-"+labName, CacheService.default_ttl_in_seconds, lab);
    		}
    	}
    	return lab;
	}
}
