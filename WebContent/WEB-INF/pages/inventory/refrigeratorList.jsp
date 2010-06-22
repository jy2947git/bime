<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="refrigeratorList.title"/></title>
    <meta name="heading" content="<fmt:message key='refrigeratorList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>
<div align="right">
<a href="<c:url value='/inventory/refrigerator/0/form.html'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="refrigerator.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_refrigeratorListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
function deleteChemicalShelves(itemId){
	new Ajax.Updater('myListDiv','<c:url value='/inventory/refrigerator/'/>' + itemId + '/delete.html?ajax=true',{method:'post'});
}
</script>

