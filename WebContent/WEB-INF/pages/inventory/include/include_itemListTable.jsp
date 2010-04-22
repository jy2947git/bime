<%@ include file="/common/taglibs.jsp"%>
<display:table name="managedItemList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="item" pagesize="25" class="table" export="false">

    <display:column property="itemCategory.name" escapeXml="true" sortable="false" titleKey="itemCategory.name" style="width: 20%"/>
    <display:column property="itemCategory.type" escapeXml="true" sortable="false" titleKey="item.type" style="width: 20%"/>
    <display:column property="amount" escapeXml="true" sortable="false" titleKey="item.stored.amount" style="width: 20%"/>
    <display:column property="storigibleUniqueId" escapeXml="true" sortable="false" titleKey="item.storage" style="width: 20%"/>
   
    <display:column property="orderItem.order.accountNumber" url="/inventory/order/orderForm.html?from=list" paramId="id" paramProperty="orderItem.order.id" escapeXml="true" sortable="false" titleKey="item.order" style="width: 20%"/>
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/inventory/itemForm.html?from=list&id=${item.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="item.edit"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="item"/></fmt:message>')){deleteItem('${item.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="item.delete"/>'/></a>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="item"/>
    <display:setProperty name="paging.banner.items_name" value="items"/>

</display:table>