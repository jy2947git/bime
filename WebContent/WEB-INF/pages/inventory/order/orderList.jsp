<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="orderList.title"/></title>
    <meta name="heading" content="<fmt:message key='orderList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/inventory/order/orderForm.html?method=Add&from=list"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="submit" style="margin-right: 5px"
        
        value="<fmt:message key="button.delete"/>"/>
</c:set>

<form name="orderListForm" method="post" action="orderListForm.html">
<input type="hidden" name="requestedMethod" value="delete"/>
<display:table name="orderList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="order" pagesize="25" class="table" export="true">
    <display:column style="width: 16%; padding-left: 15px" media="html">
        <input type="checkbox" name="selected" value="<c:out value="${order.id}"/>"/>
    </display:column>
    <display:column property="accountNumber" escapeXml="true" sortable="true" titleKey="order.accountName" style="width: 20%"
        url="/inventory/order/orderForm.html?from=list" paramId="id" paramProperty="id"/>
    <display:column property="createdDate" escapeXml="false" sortable="true" titleKey="order.createdDate" format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column property="totalPrice" escapeXml="true" sortable="true" titleKey="order.totalPrice" style="width: 20%"/> 
	<display:column property="fundName" escapeXml="true" sortable="true" titleKey="order.fundName" style="width: 20%"/> 
	<display:column property="submitDate" escapeXml="false" sortable="true" titleKey="order.submitDate"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column property="approvalDate" escapeXml="false" sortable="true" titleKey="order.approvalDate"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	
	<display:column escapeXml="true" sortable="true" titleKey="order.status" style="width: 20%"><fmt:message key="${order.status}"/></display:column> 
	


    <display:setProperty name="paging.banner.item_name" value="order"/>
    <display:setProperty name="paging.banner.items_name" value="orders"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
    <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
</display:table>

<c:out value="${buttons}" escapeXml="false" />
</form>
<script type="text/javascript">
    highlightTableRows("equipments");
</script>
