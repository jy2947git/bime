<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="itemList.title"/></title>
    <meta name="heading" content="<fmt:message key='itemList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>

<div align="right">
<a href="<c:url value='/inventory/item/0/form.html'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="item.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_itemListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteItem(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/inventory/item/'/>' + itemId + '/delete.html?ajax=true',{method:'post'});
	}
</script>
