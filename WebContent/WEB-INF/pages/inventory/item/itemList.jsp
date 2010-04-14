<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="itemList.title"/></title>
    <meta name="heading" content="<fmt:message key='itemList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/inventory/item/itemForm.html?method=Add&from=list"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="submit" style="margin-right: 5px"
        
        value="<fmt:message key="button.delete"/>"/>
</c:set>

<form name="itemListForm" method="post" action="itemListForm.html">
<input type="hidden" name="requestedMethod" value="delete"/>
<display:table name="itemList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="item" pagesize="25" class="table" export="true">
    <display:column style="width: 16%; padding-left: 15px" media="html">
        <input type="checkbox" name="selected" value="<c:out value="${item.id}"/>"/>
    </display:column>
    <display:column property="itemCategory.name" escapeXml="true" sortable="true" titleKey="itemCategory.name" style="width: 20%"
        url="/inventory/item/itemForm.html?from=list" paramId="id" paramProperty="id"/>
    <display:column property="itemCategory.type" escapeXml="true" sortable="true" titleKey="item.type" style="width: 20%"/>
    <display:column property="amount" escapeXml="true" sortable="true" titleKey="item.stored.amount" style="width: 20%"/>
    <display:column property="storageDetail" escapeXml="true" sortable="true" titleKey="item.storage" style="width: 20%"/>
   
    <display:column property="orderItem.order.accountNumber" url="/inventory/order/orderForm.html?from=list" paramId="id" paramProperty="orderItem.order.id" escapeXml="true" sortable="true" titleKey="item.order" style="width: 20%"/>

    <display:setProperty name="paging.banner.item_name" value="item"/>
    <display:setProperty name="paging.banner.items_name" value="items"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
    <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
</display:table>

<c:out value="${buttons}" escapeXml="false" />
</form>
<script type="text/javascript">
    highlightTableRows("items");
</script>
