<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="orderList.title"/></title>
    <meta name="heading" content="<fmt:message key='orderList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>

<div align="right">
<a href="<c:url value='/inventory/orderForm.html?method=Add&from=list'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="order.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_orderListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteOrder(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/inventory/include/include_orderListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId}});
	}
</script>
