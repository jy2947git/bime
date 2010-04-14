<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="equipmentList.title"/></title>
    <meta name="heading" content="<fmt:message key='equipmentList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/inventory/equipment/equipmentForm.html?method=Add&from=list"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="submit" style="margin-right: 5px"
        
        value="<fmt:message key="button.delete"/>"/>
</c:set>

<form name="equipmentListForm" method="post" action="equipmentListForm.html">
<input type="hidden" name="requestedMethod" value="delete"/>
<display:table name="equipmentList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="equipment" pagesize="25" class="table" export="true">
    <display:column style="width: 16%; padding-left: 15px" media="html">
        <input type="checkbox" name="selected" value="<c:out value="${equipment.id}"/>"/>
    </display:column>
    <display:column property="name" escapeXml="true" sortable="true" titleKey="equipment.name" style="width: 20%"
        url="/inventory/equipment/equipmentForm.html?from=list" paramId="id" paramProperty="id"/>
    <display:column property="type" escapeXml="true" sortable="true" titleKey="equipment.type" style="width: 20%"/>
    <display:column property="equiptmentCondition" escapeXml="true" sortable="true" titleKey="equipment.condition" style="width: 15%"/>
    <display:column property="contactUserName" escapeXml="true" sortable="true" titleKey="equipment.contact_user_name" style="width: 15%"/>
    <display:column property="location" escapeXml="true" sortable="true" titleKey="equipment.location" style="width: 15%"/>
    <display:column property="lastUserName" escapeXml="true" sortable="true" titleKey="equipment.last_user_name" style="width: 15%"/>
    

    <display:setProperty name="paging.banner.item_name" value="equipment"/>
    <display:setProperty name="paging.banner.items_name" value="equipments"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
    <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
</display:table>

<c:out value="${buttons}" escapeXml="false" />
</form>
<script type="text/javascript">
    highlightTableRows("equipments");
</script>
