<%@ include file="/common/taglibs.jsp"%>

<display:table name="workPlanItems" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="planItem" pagesize="25" class="table" export="false">

    <display:column property="subject" escapeXml="true" sortable="true" titleKey="workPlanItem.subject" style="width: 30%">
    	
    </display:column>
    <display:column escapeXml="true" sortable="true" titleKey="workPlanItem.status" style="width: 30%">
    	<fmt:message key="${planItem.status}"/>
    </display:column>
    <display:column property="startDate" escapeXml="false" sortable="true" titleKey="workPlanItem.startDate"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="#" onclick="return GB_showCenter('plan item','<c:url value='/project/include/include_workPlanItemForm.html?from=list&id=${planItem.id}'/>',500,500,displayWorkPlanItems)"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="planItem.edit"/>"/></a>&nbsp;
         <a href="javascript:deleteWorkPlanItem('${planItem.id}')"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="planItem.delete"/>'/></a>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="to-do"/>
    <display:setProperty name="paging.banner.items_name" value="to-do"/>

</display:table>


