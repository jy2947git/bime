<%@ include file="/common/taglibs.jsp"%>
<display:table name="managedOrderList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="order" pagesize="25" class="table" export="false">

    <display:column property="accountNumber" escapeXml="true" sortable="false" titleKey="order.accountName" style="width: 20%"/>
    
	<display:column property="totalPrice" escapeXml="true" sortable="false" titleKey="order.totalPrice" style="width: 20%"/> 
	<display:column property="submitDate" escapeXml="false" sortable="false" titleKey="order.submitDate"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column property="approvalDate" escapeXml="false" sortable="false" titleKey="order.approvalDate"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	
	<display:column escapeXml="true" sortable="false" titleKey="order.status" style="width: 20%"><fmt:message key="${order.status}"/></display:column> 
	
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/inventory/order/${order.id}/form.html'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="order.edit"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="order"/></fmt:message>')){deleteOrder('${order.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="order.delete"/>'/></a>
    </display:column>

    <display:setProperty name="paging.banner.item_name" value="order"/>
    <display:setProperty name="paging.banner.items_name" value="orders"/>

</display:table>