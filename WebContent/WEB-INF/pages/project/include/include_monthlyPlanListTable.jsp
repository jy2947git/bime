

<display:table name="workplanList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="workPlan" pagesize="25" class="table" export="true">

    <display:column escapeXml="true" sortable="true" titleKey="workplan.identity" style="width: 20%">
        <fmt:message key="${workPlan.planIdentity}"/>
    </display:column>
    <display:column escapeXml="true" sortable="false" titleKey="workplan.status" style="width: 20%">
    	<fmt:message key="${workPlan.status}"></fmt:message>
    </display:column>
    <display:column property="managedProject.name" escapeXml="true" sortable="true" titleKey="workplan.project" style="width: 20%"/>
    <display:column property="lastUpdateDate" escapeXml="false" sortable="true" titleKey="workplan.last_update_date"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 

	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/project/workplanForm.html?from=list&id=${workPlan.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="planItem.edit"/>"/></a>&nbsp;
         <a href="javascript:deleteWorkPlan('${workPlan.id}')"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="workplan.delete"/>'/></a>
    </display:column>
    
    <display:setProperty name="paging.banner.item_name" value="workplan"/>
    <display:setProperty name="paging.banner.items_name" value="workplan"/>

</display:table>
<script type="text/javascript">
	function deleteMonthlyWorkPlan(planId){
		new Ajax.Updater('workPlanListTableDiv','<c:url value='/project/include/include_monthlyWorkPlanListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:planId,parentId:null}});
	}
	function displayMonthlyWorkPlanList(){
		new Ajax.Updater('workPlanListTableDiv','<c:url value='/project/include/include_monthlyWorkPlanListTable.html'/>',{method:'get', parameters:{from:'list',parentId:<c:out value='${workPlan.id}'/>}});
	}
	displayMonthlyWorkPlanList();
</script>