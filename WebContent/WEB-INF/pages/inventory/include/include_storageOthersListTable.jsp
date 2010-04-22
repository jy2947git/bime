<%@ include file="/common/taglibs.jsp"%>

<display:table name="storageOthersList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="storageOthers" pagesize="25" class="table" export="false">

    <display:column property="name" escapeXml="true" sortable="false" titleKey="storage.name" style="width: 20%"/>
    <display:column property="type" escapeXml="true" sortable="false" titleKey="storage.type" style="width: 20%"/>
    <display:column property="location" escapeXml="true" sortable="false" titleKey="storage.location" style="width: 15%"/>
    <display:column property="contactPerson.fullName" escapeXml="true" sortable="false" titleKey="storage.contactPerson" style="width: 15%"/>
    
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/inventory/othersForm.html?from=list&id=${storageOthers.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="storage.edit"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="storage section"/></fmt:message>')){deleteStorageOthers('${storageOthers.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="storage.delete"/>'/></a>
    </display:column>
    
    <display:setProperty name="paging.banner.item_name" value="storage"/>
    <display:setProperty name="paging.banner.items_name" value="storages"/>

</display:table>