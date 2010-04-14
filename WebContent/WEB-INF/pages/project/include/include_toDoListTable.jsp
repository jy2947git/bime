<%@ include file="/common/taglibs.jsp"%>

<display:table name="toDoList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="toDo" pagesize="25" class="table" export="false">

    <display:column property="subject" escapeXml="true" sortable="true" titleKey="toDo.subject" style="width: 30%">
    	
    </display:column>
    <display:column escapeXml="true" sortable="true" titleKey="toDo.status" style="width: 30%">
    	<fmt:message key="${toDo.status}"/>
    </display:column>
    <display:column property="startDate" escapeXml="false" sortable="true" titleKey="toDo.startDate"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/project/toDoForm.html?from=list&id=${toDo.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="planItem.edit"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="to-do"/></fmt:message>')){deleteToDo('${toDo.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="toDo.delete"/>'/></a>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="to-do"/>
    <display:setProperty name="paging.banner.items_name" value="to-do"/>

</display:table>


