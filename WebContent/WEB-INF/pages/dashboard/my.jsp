<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="dashboard.title"/></title>
   
    <meta name="menu" content="DashboardMenu"/>
</head>
<h1><fmt:message key='dashboard.my_project'/></h1>
<display:table name="myProjectList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="project" pagesize="25" class="table" export="true">

    <display:column property="name" escapeXml="true" sortable="true" titleKey="project.name" style="width: 20%"
        url="/project/projectForm.html?from=list" paramId="id" paramProperty="id"/>
</display:table>

<h1><fmt:message key='dashboard.orders_submitted_by_me'/></h1>

<display:table name="mySubmittedOrderList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="order" pagesize="25" class="table" export="true">
    <display:column property="accountNumber" escapeXml="true" sortable="true" titleKey="order.accountName" style="width: 20%"
        url="/inventory/order/orderForm.html?from=list" paramId="id" paramProperty="id"/>
    <display:column property="createdDate" escapeXml="false" sortable="true" titleKey="order.createdDate" format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column property="totalPrice" escapeXml="true" sortable="true" titleKey="order.totalPrice" style="width: 20%"/> 
	<display:column property="fundName" escapeXml="true" sortable="true" titleKey="order.fundName" style="width: 20%"/> 
	<display:column property="submitDate" escapeXml="false" sortable="true" titleKey="order.submitDate"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	
	<display:column escapeXml="true" sortable="true" titleKey="order.status" style="width: 20%"><fmt:message key="${order.status}"/></display:column> 

</display:table>
<h1><fmt:message key='dashboard.orders_for_my_approval'/></h1>

<display:table name="myForApprovalOrderList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="order" pagesize="25" class="table" export="true">
    <display:column property="accountNumber" escapeXml="true" sortable="true" titleKey="order.accountName" style="width: 20%"
        url="/inventory/order/orderForm.html?from=list" paramId="id" paramProperty="id"/>
    <display:column property="createdDate" escapeXml="false" sortable="true" titleKey="order.createdDate" format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column property="totalPrice" escapeXml="true" sortable="true" titleKey="order.totalPrice" style="width: 20%"/> 
	<display:column property="fundName" escapeXml="true" sortable="true" titleKey="order.fundName" style="width: 20%"/> 
	<display:column property="submitDate" escapeXml="false" sortable="true" titleKey="order.submitDate"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	
	<display:column escapeXml="true" sortable="true" titleKey="order.status" style="width: 20%"><fmt:message key="${order.status}"/></display:column> 

</display:table>