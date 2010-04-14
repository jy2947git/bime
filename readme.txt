//-----------------------------------
//
//Development Environment Setup
//
//
//----------------------------------
1, download and install JDK from Sun website, choose the latest and install, for example c:\\jdk1.6.0_17
2, download latest eclipse from http://www.eclipse.org/downloads/, choose the "Eclipse IDE for Java EE Developers", unzip to c: drive
3, download latest tomcat from http://tomcat.apache.org/download-60.cgi, choose the zip file. Unzip to c:
4, verify JDK installation. Start a DOS command, enter "java -version", it should display the correct java version.
5, configure Tomcat. Enter the tomcat/bin directory, edit the "catalina.bat" file. Add below after the "rem --------"
set JAVA_HOME=c:\\jdk1.6.0_17 (or whatever the path where jdk is installed)
6, verify Tomcat. Start a DOS command, enter into tomcat bin directory, for example, c:/apache-tomcat-6.0.20/bin, run "startup.bat". You should see the tomcat start up messages. Should not see any errors. Start a browser, and try url http://localhost:8080, you should see the tomcat page.
Start another DOS command, enter to tomcat bin directory, and run "shutdown.bat", to shutdown the tomcat server.
7, download mysql server from http://dev.mysql.com/downloads/mysql/5.1.html. Select all the default setting when installing mysql.
8, start and verify mysql server. Start a Dos command, enter the mysql directory, for example "c:\\mysql-5.1.37-win32\bin", enter command "mysqld --console", this will start mysql database server. You should not see any error message.
keep mysql running. 
9, start Eclipse by double-clicking the c:\\eclipse\eclipse.exe. You will be asked to enter the workspace name. Because this is the first time you run Eclipse, and you don't have the workspace directory yet, just enter "c:\\workspace",and Eclipse will create the new directory for you.
10, install the Subclipse plug-in,which is used to the Subversion server. In Eclipse, click "help" then "software updates...", then choose the "Available Software" tab, click "Add Site", then enter url "http://subclipse.tigris.org/update_1.6.x", click "OK".
Next, you can check the Subclipse plug-in and click "Install". Accepet term, and click NEXT to install. Eclipse will auto-restart to make the new plug-in effective.
11, After Eclipse restarted, click "window" then "Open Perspective" then "Other...", then choose the "SVN Repository Exploring" (if you don't see it in the perspective list, that means the Subclipse plug-in was not installed correctly). Click "OK"
Once you are in the SVN perspecitve, at left side window, in the "SVN repositories" tab, right click mouse, choose "New" -> "Repository Location...", and enter below URL
"http://focaplo.dnsdojo.com:8780/svn/svn-repository", click "Finish". Now Subclipse will try to connect to SVN server and list the svn repository.
You'd see the "http://focaplo...." in the left side window. Select it, right click mouse, and choose "refresh"
Now you will see the projects in this SVN repository, for example, "bime".
Choose "bime", and right-click mouse, choose "check out...", and on the check-out page, click "Finish". Now Subclipse will try to download the "bime" project source code into your local Eclipse workspace.
Normally you'd see the "console" window displaying the check-out progress.
After the downloading is finished, click "window->open perspective->other" and choose the "Java EE" perspective. (maybe Eclipse will ask you to change to Java EE perspective after the downloading)
You will be able to see a new project "bime" is now in the left-side "Project Explorer" window. (or you can click "window" -> "show view" and choose "Project Explorer" view)
12, In the "Project Explorer" window, explan the "bime" project, expand "tools", select file "build_hibernate_tools.xml", right click and choose "Run As", then choose the 3rd option "3 Ant Build...". On the next "Build Target" screen, uncheck the "recreateDatabase", and check the "initiateDatabase", and click "Run"
now you will see in the console window that Ant build script is being executed to compile the model classes, and generate DDL and create the tables in the MySql database.
You'd not see error message for example "build failed..."
13, install sql client tool SQLSquirrel. Download the jar file from http://www.squirrelsql.org/#installation, and start a Dos command, enter the directory where you downloaded the jar and enter command "java -jar squirrel-sql-<version>-install.jar".
choose "start" -> "all programs" ->"SQuirrel SQL Client" -> to start the software. First need to configure the MySql jdbc driver.
In the "Drivers" window, select the "MySQL Driver" in the list, right click and choose "modify driver"
In the next window, choose the "Extra Class Path" tab, and click "Add", you will be asked to enter the path to the MySQL jdbc driver jar file.
Now browse to the path c:\\workspace\\bime\\WebContent\\WEB-INF\\lib\\mysql-connector-java-xxx.jar" and click "Open"
Make sure the "Class Name:" is "com.mysql.jdbc.Driver".
Click "Alias" -> "New Alias...", enter Name "local", Driver select "MySQL Driver", URL "jdbc:mysql://localhost/mydb", User Name "mydbuser", password "mydbpassword". Click "Ok".
Now in the "Alias" choose "local" and right click to choose "Connect...", you will be able to connect to local MySql server.
You will be prompt a new window, expand "local" -> "mydb" -> "Table", you shuld see all the tables of Bime project.
14,Back to Eclipse to configure the tomcat. Choose "window" -> "Preferences". At left side of the Preferences window, expand "Server", and choose "Runtime Environments", click "Add" button. In the next window, expand "Apache" and choose "Apache Tomcat 6.0" and click "Next", on the next window, click "Browse" and select the directory where you have Tomcat installed, for example, "c:\\apache-tomcat-6.0.20" (this is the ROOT tomcat directory), click Ok.
then, select "Validation" at the left side of the "Preferences" window, and check "Suspend all validators", and click "Ok"
15,Now to create a local tomcat server for testing in Eclipse. Select "window" -> "Show View" and choose "Servers". You will see the "Server" view window at the right bottom side of Eclipse. Move mouse into that view and right click and choose "New" -> "Server".
In the next windown, select "Apache" -> "Tomcat v6.0 server", click "next", then click Finish. You will see a new Tomcat instance show up in the "Server" view window.
Select the new tomcat instance and right click to choose "Start", you will see the tomcat starting up.
16,Now at the left side "Project Explorer" window, select the "bime" project, right click mouse to choose "Run As" then "Run on Server", click Finish on the next window. This will install the bime project on the local tomcat instance. In the "Console" view window, you will see tomcat being restarted and bime being installed.
17,Start FireFox and enter url "http://localhost:8080/bime", you will see the login page. Below default users have already been set up
user/test
super/test
admin/admin


//----------------------------------------------------
// Frameworks used by Bime
//----------------------------------------------------
Bime is developed on the skeleton of AppFuse 2.0 (http://appfuse.org/display/APF/Home). It is slightly customized to use Ant instead of Maven and the project structure is changed to integrate with Eclipse.
Below framewors/technologies are used by Bime/AppFuse
Spring 2.0 including Spring Core, Spring MVC, Taglib, Test
Hibernate for ORM (JPA)
JUnit 4 for unit testing
Struts-Menu for Menu
Velocity for decorating html code
SiteMesh
Acegi for security (authorization, user etc)
prototype for java-script
scrip.tu.lous for java-script
jquery for java-script (date picker)
dwr for AJAX
GreyBox for popup dialog


//----------------------------------------------------
// Bime project structure
//
//----------------------------------------------------
Here is the tree stucture of bime project

	bime
		---Java Resources
				--- src
					---com.focaplo.myfuse.dao	(Data Access Objects, JPA with Hibernate)
					---com.focaplo.myfuse.model (Model or Domain classes)
					---com.focaplo.myfuse.service (Business services)
					---com.focaplo.webapp.controller (Web Controllers of Spring MVC)
				--- resources
					---applicationContext-xxx.xml (Spring context files)
					---ApplicationResources.properties (Labels)
					---hibernate.cfg.xml (Hibernate)
					---jdbc.properties (Database)
					---mail.properties (SMTP)
					---xxx.vm (velocity tempaltes)
		---deployment (configuration files on production linux server including shellscripts, sql, etc)
				----amazon
				----apache
				----archirved
				----automation
				----exim4
				----haproxy
				----hornetq
				----linux
				----mysql
				----tomcat
				
		---tools
				---build_hibernate_tools.xml (Ant script to automatically generate DDL and create/refresh LOCAL mysql database)
				---hibernate_tools.properties (Ant property files for the build_hibernate_tools.xml)
				---hibernate.cfg.xml (hibernate configuration for the build_hibernate_tools.xml)
				--- *.jar (ant jar files)
		---WebContent
			---admin
			---common
				---footer.jsp
				---header.jsp
				---menu.jsp (display menu)
				---messages.jsp
				---meta.jsp
				---taglibs.jsp
			---decorators
				---default.jsp (the base html tempalte)
			---images
			---META-INF
			---scripts
			---styles
			---WEB-INF
				---lib
				---pages
					---admin (all pages under Admin tab)
					---dashboard (all pages under Dashboard tab)
					---grant (all pages under Grant tab)
					---inventory (all pages under Inventory tab)
						---include (the included jsp files on inventory pages, for example, all the table lists are in included jsp)
					---lab (all pages under Lab tab)
					---project (all pages under Project tab)
						---include
					---report (all pages under Report tab)
				appfuse.tld
				applicationContext.xml
				applicationContext-validation.xml
				decorators.xml
				dispatcher-servlet.xml (Spring MVC beans and url mappings)
				dwr.xml
				menu-config.xml (menu definations)
				resin-web.xml
				security.xml
				sitemesh.xml
				urlrewrite.xml
				validation.xml (form validations)
				validator-rules.xml
				validator-rules-consutom.xml
				web.xml
				xfire-servlet.xml
		bsh-2.0b1.jar
		build.properties
		build.xml (Ant build script)
		readme.txt

//------------------------------------------------------
// Example of development case 1
//------------------------------------------------------
Assume you are adding a attribute to the "Project", for example, "Fund". You will typically take below steps
1, edit the model class to add the new property
Open the com.focaplo.myfuse.model.ManagedProject
Add below
		@Column
		private String fund;
@Column annotation indicates this property is persistable and will be a table column
(see http://docs.jboss.org/hibernate/stable/annotations/reference/en/html/ for JPA/Hibernate Annotations)

2, No need to change Dao interface/implementation for example ProjectDao, ProjectDaoHibernate since we are not doing anything special to this new property, all existing dao functions should be enough.
3, select the tools/build_hiberbate_tools.xml, and run as Ant script, choose "recreateDatabase" target. This Ant script will read the model classes and re-generate the DDL (chema.sql) and update the local MySql with the new column.
Note, all the existing data will be wiped out except those default records.
4, run junit test case if there is in the com.focaplo.myfuse.dao package.
5, No need to change service interface/implementation for example ProjectManager, ProjectManagerImpl since nothing of this new property need special care.
6, Edit /WebContent/WEB-INF/pages/project/projectForm.jsp to add this new field, for example,
		<div>
            <appfuse:label styleClass="desc" key="project.fund"/>
            <form:errors path="fund" cssClass="fieldError"/>
            <form:input path="fund" id="fund" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
note- the "path" MUST BE EXACTLY SAME AS the property name in the ManagedProject class.
The "project.fund" is a label key, which you can add to resources/ApplicationResources.properties, ie, project.fund=Fund
7, Assume this attribute will also be displayed in the project list table. 
Edit the WebContent/WEB-INF/pages/project/include/include_projectListTable.jsp and add below
<display:column property="fund" escapeXml="true" sortable="false" titleKey="project.fund" style="width: 20%"/>
note- the property value in above line MUST BE EXACTLY THE SAME AS the property name in the ManagedProject domain class.
8, Assume this is a required field, edit WEB-INF/validation.xml, find the <form name="ManagedProject">, add below
			<field property="fund"
                     depends="required">

                  <arg0 key="project.fund"/>
              </field>
same as before, make sure the property match the value in the form jsp.
9, Edit the resources/ApplicationResources.properties to add any labels
10, retart server.

//------------------------------------------------------
// Example of development case 2
//------------------------------------------------------
Assume you are adding a new page under Project tab, for example, "ticket". Basically a project can have many tickets and user can create/display/edit tickets.
A ticket has properties like "assignDate", "endDate", "assignee", "status"
1, create a new class in com.focaplo.myfuse.model package, for example, Ticket, make it extend the BaseObject class and implement Seriablizable
Add @Entity to the class level to indicate this class will be mapped to a table
Add an ID field which will be identity type in MySQL
		@Id 
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private Long id;
Add all the properties with @Column annotation, and getters and setters.
2, take care of the one-to-many relationship between Project and Ticket.
Add below Many-To-One annotation in the Ticket class
	@ManyToOne
	@JoinColumn(name="managed_project_id")
	private ManagedProject managedProject;
Add below One-To-Many annotation in the ManagedProject class
	@OneToMany(mappedBy="managedProject",cascade=CascadeType.ALL)
	private Set<Ticket> tickets=new HashSet<Ticket>();
3, Add the new class to the Hibernate configuration files, update both /tools/hibernate.cfg.xml and /resources/hibernate.cfg.xml, add below
	<mapping class="com.focaplo.myfuse.model.Ticket"/>
	
4, run the tools/build_hibernate_tools.xml to regenerate DDL and update the local database with the new table.
5, add coupld of unit test cases to the ProjectDao to test CRUD operations of Ticket and Project.
6, add methods like "getTicketsOfProject" to ProjectDao and ProjectManager, this will be used to retreive list of Tickets to display in the List page.
7, Now the middel tier and back tier are ready, move on to the front end, fist, need a new List-Controller to manage the "List" page and a Form-Controller to manage the "form" page.
First create TicketListController in the com.focaplo.mufuse.webapp.controller package and make it extend the BaseListController. Because the BaseListController has been set up to take care of the list page, the TicketListController only need add one method to return the Ticket.class.
Then create TicketFormController to extend BaseFormController. Copy and paste the ProtocolFormController, and make below changes:
replace all the "managedProtocol" with "ticket", repalce all the "ManagedProtocol" with "Ticket", and remove those lines only applying to Protocol.
8, Next to configure the new web controllers to integrate with Spring MVC. Edit the /WebContent/WEB-INF/dispatcher-servlet.xml, add below Spring bean definations
	<bean id="ticketListController" class="com.focaplo.myfuse.webapp.controller.TicketListController">
		<property name="includedListView" value="project/include/include_ticketListTable"/>
		<property name="listView" value="project/protocolList"/>
	</bean>
	<bean id="ticketFormController" class="com.focaplo.myfuse.webapp.controller.TicketFormController">
		<property name="projectManager" ref="projectManager"/>
		<property name="validator" ref="beanValidator"/>
        <property name="formView" value="/project/ticketForm"/>
        <property name="successView" value="redirect:ticketList.html"/>
        <property name="cancelView" value="redirect:tikcketList.html"/>
	</bean>
Note, in above xml, we are referring to 3 jsp files: project/ticketList.jsp, project/ticketForm.jsp, and project/include/include_ticketListTable.jsp
(although you see .html as path, the real file is actually jsp files. The Spring MVC will interpret all the .html and load the correponding jsp files)

9, Still in the dispatcher-servlet.xml, add below URL mapping rules
				/project/ticketList.html=ticketListController
                /project/include/include_ticketListTable.html=ticketListController
                /project/ticketForm.html=ticketFormController
   This basically tells Spring MVC to execute the Ticket List/Form controllers for above urls (note the .html)
10, Now create 2 jsp files - /WebContent/WEB-INF/pages/project/ticketList.jsp and /project/include/include_ticketListTable.jsp.
Copy/paste the protocolList.jsp and include_protocolListTable.jsp to the new files.Change "protocol" to "ticket"
The ticketList.jsp is pretty simple, it just defines the "add ticket" and "delete ticket" and include the include_ticketListTable.jsp to display the tickets.
Note the "add" is traditional href which will display the form page; the "delete" will be asynchronous through the Prototype framework's Ajax support
	function deleteTicket(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/project/include/include_ticketListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId}});
	}
Above javascript will send request to url "/project/include/include_ticketListTable.html?from=list&id=12345", which will be handled by the TicketListController. The TicketListController will delete the ticket through ProjectManager and forward to jsp "include/include_ticketListTable.jsp", to display the updated tickets table; the HTML code then will be used to replace the <div> with Ajax.
The include_ticketListTable.jsp is to display the tickets. 
	<display:table name="ticketList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="protocol" pagesize="25" class="table" export="false">

    <display:column property="status" escapeXml="true" sortable="false" titleKey="ticket.status" style="width: 30%"/>
	...
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/project/ticketForm.html?from=list&id=${ticket.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="ticket.edit"/>"/></a>&nbsp;
         <a href="javascript:deleteTicket('${ticket.id}')"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="ticket.delete"/>'/></a>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="ticket"/>
    <display:setProperty name="paging.banner.items_name" value="tickets"/>
</display:table>
Note, the <display:table name="ticketList" the "ticketList" is the request attribute created by the controller. The rule of name is model class name (first charactor small-case) plus "List", for example, "Ticket" will be "ticketList", "ManagedProject" will be "managedProjectList" (refer to the BaseListController)

11, create the /WEB-INF/pages/project/ticketForm.jsp and copy/paste the protocolForm.jsp into the new file.
Make sure the comamnd-name "ticket" match the command-name defined in the FormController class.
<spring:bind path="ticket.*">
<form:form commandName="ticket" method="post" action="ticketForm.html" onsubmit="return onFormSubmit(this)" id="ticketForm">
<script type="text/javascript">
function onFormSubmit(theForm) {
    return validateTicket(theForm);
}
</script>
<v:javascript formName="ticket" staticJavascript="false"/>

12,Now that we have the struts-validation turned on the ticket page, we need to add the ticket form to /WEB-INF/validation.xml
	<form name="ticket">
              <field property="status"
                     depends="required">
                  <arg0 key="ticket.status"/>
              </field>
    </form>
    note the form name is "ticket" instead of "ticketForm", this matches the return validateTicket(theForm); and <v:javascript formName="ticket" staticJavascript="false"/> in form jsp.
13, now we are ready, only need to add a new menu item to the /WEB-INF/menu-config.xml, add below to "ProjectSubMenu"
	<Item name="tickets" title="menu.tickets" page="/project/ticketList.html"></Item>
14, restart server and test
15, add whatever labels to the ApplicationResources.properties

//-----------------------------------------------
// Steps to set up for new client
//-----------------------------------------------
After a new client sign up, execute below steps to set up on host server
1, log on host server and enter /usr/local/bime-home
2, run script "./newClient.sh client-name", (client-name is the simple name of the new client), for example, "newClient.sh uiclab" for UIC Medical Lab.
Note, the client name is critical, it is used as directory name, web context name, and used in all the other scripts. Make sure it does have any spcial characters except '_', '-', and no space is allowed.
3, Now there is a new directory created under bime-home. Then enter the new directory and vi the make.sh, edit the build properties like "customer.name", "customer.company", "customer.tag" and save.

//----------------------------------------------
// Steps to build bime app for client
//----------------------------------------------
1, log on host server. enter /usr/local/bime-home
2, run script "./build.sh client-name", this script will download the source code from SVN server, build the distribution, and customize it.

//----------------------------------------------
// Steps to deploy app
//----------------------------------------------
1, log on host server, enter /usr/local/bime-home
2a, if need to recreate database, run script "./deploy.sh client-name 1"
2b, if no need to recreate database, run script "./deploy.sh client-name 2"
3, shutdown and restart tomcat server - /usr/local/tomcat6-1/bin/shutdown.sh and /usr/local/tomcat6-1/bin/startup.sh
check tomcat start up log with command "tail -n 200 /usr/local/tomcat6-1/log/catalina.out"

