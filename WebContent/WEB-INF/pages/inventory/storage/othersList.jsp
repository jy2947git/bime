<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="refrigeratorList.title"/></title>
    <meta name="heading" content="<fmt:message key='storageOthersList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/inventory/storage/othersForm.html?method=Add&from=list"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="submit" style="margin-right: 5px"
        
        value="<fmt:message key="button.delete"/>"/>
</c:set>

<form name="storageOthersListForm" method="post" action="othersListForm.html">
<input type="hidden" name="requestedMethod" value="delete"/>
<display:table name="storageOthersList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="storageOthers" pagesize="25" class="table" export="true">
    <display:column style="width: 16%; padding-left: 15px" media="html">
        <input type="checkbox" name="selected" value="<c:out value="${storageOthers.id}"/>"/>
    </display:column>
    <display:column property="name" escapeXml="true" sortable="true" titleKey="storage.name" style="width: 20%"
        url="/inventory/storage/othersForm.html?from=list" paramId="id" paramProperty="id"/>
    <display:column property="type" escapeXml="true" sortable="true" titleKey="storage.type" style="width: 20%"/>
    <display:column property="location" escapeXml="true" sortable="true" titleKey="storage.location" style="width: 15%"/>
    <display:column property="contactPerson.fullName" escapeXml="true" sortable="true" titleKey="storage.contactPerson" style="width: 15%"/>
    

    <display:setProperty name="paging.banner.item_name" value="storage"/>
    <display:setProperty name="paging.banner.items_name" value="storages"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
    <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
</display:table>

<c:out value="${buttons}" escapeXml="false" />
</form>
<script type="text/javascript">
    highlightTableRows("storages");
</script>
