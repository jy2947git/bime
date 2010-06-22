<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="refrigeratorList.title"/></title>
    <meta name="heading" content="<fmt:message key='storageOthersList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>

<div align="right">
<a href="<c:url value='/inventory/storageOthers/0/form.html'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="storage.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_storageOthersListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
function deleteStorageOthers(itemId){
	new Ajax.Updater('myListDiv','<c:url value='/inventory/storageOthers/'/>' + itemId + '/delete.html?ajax=true',{method:'post'});
}
</script>
