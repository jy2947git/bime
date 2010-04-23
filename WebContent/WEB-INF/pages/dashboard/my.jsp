<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="dashboard.title"/></title>
   
    <meta name="menu" content="DashboardMenu"/>
</head>
<h2><fmt:message key='dashboard.my_meeting'/></h2>
<display:table name="myMeetingList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="meeting" pagesize="25" class="table" export="false">

    <display:column property="startDate" escapeXml="false" sortable="false" titleKey="meeting.startDate" format="{0,date,MM/dd/yyyy}" style="width: 20%"
        url="/lab/meetingForm.html?from=list" paramId="id" paramProperty="id"/>
        <display:column property="startHourAndMinute" escapeXml="false" sortable="true" titleKey="meeting.startTime" style="width: 20%"/>
     <display:column property="subject" escapeXml="true" sortable="true" titleKey="meeting.subject" style="width: 60%"/>
     
</display:table>


<h2><fmt:message key='dashboard.my_project'/></h2>
<display:table name="myProjectList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="project" pagesize="25" class="table" export="false">

    <display:column property="name" escapeXml="true" sortable="false" titleKey="project.name" style="width: 60%"
        url="/project/projectForm.html?from=list" paramId="id" paramProperty="id"/>
     <display:column escapeXml="true" sortable="true" titleKey="project.status" style="width: 20%"><fmt:message key="${project.status}"/></display:column>
</display:table>

<h2><fmt:message key='dashboard.orders_submitted_by_me'/></h2>

<display:table name="mySubmittedOrderList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="order" pagesize="25" class="table" export="false">
    <display:column property="accountNumber" escapeXml="true" sortable="false" titleKey="order.accountName" style="width: 20%"
        url="/inventory/orderForm.html?from=list" paramId="id" paramProperty="id"/>
    <display:column property="createdDate" escapeXml="false" sortable="false" titleKey="order.createdDate" format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column property="totalPrice" escapeXml="true" sortable="false" titleKey="order.totalPrice" style="width: 20%"/> 
	<display:column property="fundName" escapeXml="true" sortable="false" titleKey="order.fundName" style="width: 20%"/> 
	<display:column property="submitDate" escapeXml="false" sortable="true" titleKey="order.submitDate"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	
	<display:column escapeXml="true" sortable="true" titleKey="order.status" style="width: 20%"><fmt:message key="${order.status}"/></display:column> 

</display:table>
<h2><fmt:message key='dashboard.orders_for_my_approval'/></h2>

<display:table name="myForApprovalOrderList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="order" pagesize="25" class="table" export="false">
    <display:column property="accountNumber" escapeXml="true" sortable="false" titleKey="order.accountName" style="width: 20%"
        url="/inventory/orderForm.html?from=list" paramId="id" paramProperty="id"/>
    <display:column property="createdDate" escapeXml="false" sortable="false" titleKey="order.createdDate" format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column property="totalPrice" escapeXml="true" sortable="false" titleKey="order.totalPrice" style="width: 20%"/> 
	<display:column property="fundName" escapeXml="true" sortable="false" titleKey="order.fundName" style="width: 20%"/> 
	<display:column property="submitDate" escapeXml="false" sortable="false" titleKey="order.submitDate"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	
	<display:column escapeXml="true" sortable="true" titleKey="order.status" style="width: 20%"><fmt:message key="${order.status}"/></display:column> 

</display:table>