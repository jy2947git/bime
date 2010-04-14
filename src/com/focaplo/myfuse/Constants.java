package com.focaplo.myfuse;


/**
 * Constant values used throughout the application.
 * 
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class Constants {
    //~ Static fields/initializers =============================================

    /**
     * The name of the ResourceBundle used in this application
     */
    public static final String BUNDLE_KEY = "ApplicationResources";

    /**
     * File separator from System properties
     */
    public static final String FILE_SEP = System.getProperty("file.separator");

    /**
     * User home from System properties
     */
    public static final String USER_HOME = System.getProperty("user.home") + FILE_SEP;

    /**
     * The name of the configuration hashmap stored in application scope.
     */
    public static final String CONFIG = "appConfig";

    /**
     * Session scope attribute that holds the locale set by the user. By setting this key
     * to the same one that Struts uses, we get synchronization in Struts w/o having
     * to do extra work or have two session-level variables.
     */
    public static final String PREFERRED_LOCALE_KEY = "org.apache.struts2.action.LOCALE";

    /**
     * The request scope attribute under which an editable user form is stored
     */
    public static final String USER_KEY = "userForm";

    /**
     * The request scope attribute that holds the user list
     */
    public static final String USER_LIST = "userList";

    /**
     * The request scope attribute for indicating a newly-registered user
     */
    public static final String REGISTERED = "registered";

    /**
     * The name of the Administrator role, as specified in web.xml
     */
    public static final String ADMIN_ROLE = "ROLE_ADMIN";

    /**
     * The name of the User role, as specified in web.xml
     */
    public static final String USER_ROLE = "ROLE_USER";

    /**
     * The name of the user's role list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String USER_ROLES = "userRoles";

    /**
     * The name of the available roles list, a request-scoped attribute
     * when adding/editing a user.
     */
    public static final String AVAILABLE_ROLES = "availableRoles";

    /**
     * The name of the CSS Theme setting.
     */
    public static final String CSS_THEME = "csstheme";

	public static final String EQUIPTMENT_LIST = "equipmentList";

	public static final String ITEM_LIST = "itemList";

	public static final String REFREGIRATOR_LIST = "refrigeratorList";

	public static final String STORAGE_OTHERS_LIST = "storageOthersList";

	public static final String GRANT_LIST = "grantList";

	public static final String PROJECT_LIST = "projectList";

	public static final String ORDER_LIST = "orderList";

	
	public static final String order_draft="1_order_draft";
	public static final String order_submitted="2_order_submitted";
	public static final String order_approved="5_order_approved";
	public static final String order_paid="7_order_paid";
	public static final String order_inventoried = "9_order_inventoried";

	public static final String order_rejected = "4_order_rejected";

	public static final String order_cancelled = "0_order_cancelled";
	
	public static final String SUPER_USER_ROLE = "ROLE_SUPER_USER";

	public static final String ERROR_NO_SUER_USER_TO_SUBMIT_ORDER = "ERROR_NO_SUER_USER_TO_SUBMIT_ORDER";

	public static final String ITEM_CATEGORY_LIST = "itemCategoryList";

	public static final String PRINCIPAL_KEY = "principal_key";

	public static final String default_inventory_threshold = "10";

	public static final String WORK_STATUS_NOT_STARTED = "work.unstarted";
	public static final String WORK_STATUS_IN_PROGRESS = "work.in_progress";
	public static final String WORK_STATUS_PAUSE = "work.pause";
	public static final String WORK_STATUS_COMPLETED = "work.completed";
	public static final String WORK_STATUS_UNCOMPLETED = "work.uncompleted";

	public static final String PROTOCOL_LIST = "protocolList";

	public static final String NOTE_LIST ="noteList";

	public static final String WORKPLAN_LIST = "workplanList";

	public static final String WORKPLAN_MONTHLY = "monthly";

	public static final String WORKPLAN_WEEKLY = "weekly";

	public static final String PROJECT_STATUS_ACTIVE = "project.active";

	public static final String PROJECT_STATUS_PENDING = "project.pending";

	public static final String PROJECT_STATUS_FINISHED = "project.finished";

	public static final String WORKPLAN_ITEM_LIST = "workPlanItems";

	public static final String TODO_LIST = "toDoList";

	public static final String WORK_LOG_LIST = "workLogList";
}
