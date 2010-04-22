<%@ include file="/common/taglibs.jsp"%>

<display:table name="refrigeratorList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="refrigerator" pagesize="25" class="table" export="false">

    <display:column property="name" escapeXml="true" sortable="false" titleKey="refrigerator.name" style="width: 20%"/>
    <display:column property="type" escapeXml="true" sortable="false" titleKey="refrigerator.type" style="width: 20%"/>
    <display:column property="location" escapeXml="true" sortable="false" titleKey="refrigerator.location" style="width: 15%"/>
    <display:column property="contactPerson.fullName" escapeXml="true" sortable="false" titleKey="refrigerator.contactPerson" style="width: 15%"/>
    
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/inventory/refrigeratorForm.html?from=list&id=${refrigerator.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="refrigerator.edit"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="refrigerator"/></fmt:message>')){deleteRefrigerator('${refrigerator.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="refrigerator.delete"/>'/></a>
    </display:column>
    
    <display:setProperty name="paging.banner.item_name" value="refrigerator"/>
    <display:setProperty name="paging.banner.items_name" value="refrigerators"/>

</display:table>