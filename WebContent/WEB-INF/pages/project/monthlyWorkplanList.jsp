<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="monthly.workplan.list.title"/></title>
    <meta name="heading" content="<fmt:message key='monthly.workplan.list.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/project/workplanForm.html?method=Add&from=list"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="submit" style="margin-right: 5px"
        
        value="<fmt:message key="button.delete"/>"/>
</c:set>

<form name="workPlanListForm" method="post" action="workPlanListForm.html">
<input type="hidden" name="requestedMethod" value="delete"/>
<div align="right">
<a href="<c:url value="/project/workplanForm.html?method=Add&from=list"/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="button.add_monthly_plan"/></a>
</div>
<display:table name="workplanList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="workPlan" pagesize="25" class="table" export="true">

    <display:column escapeXml="true" sortable="true" titleKey="workplan.identity.monthly" style="width: 20%">
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


</form>

