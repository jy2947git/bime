<%@ include file="/common/taglibs.jsp"%>
<div width="100%">
<menu:useMenuDisplayer name="Velocity" config="cssHorizontalMenu.vm" permissions="rolesAdapter">
<ul id="primary-nav" class="menuList">
  
   <menu:displayMenu name="DashboardMenu"/>
    <menu:displayMenu name="LabMenu"/>
    <menu:displayMenu name="ProjectMenu"/>

  
    <menu:displayMenu name="AdminMenu"/>
   <menu:displayMenu name="InventoryMenu"/>
   <menu:displayMenu name="ReportMenu"/>
</ul>
</menu:useMenuDisplayer>
</div>
