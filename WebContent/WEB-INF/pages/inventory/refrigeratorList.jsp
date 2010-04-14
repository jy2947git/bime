<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="refrigeratorList.title"/></title>
    <meta name="heading" content="<fmt:message key='refrigeratorList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>
<div align="right">
<a href="<c:url value='/inventory/refrigeratorForm.html?method=Add&from=list'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="refrigerator.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_refrigeratorListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteRefrigerator(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/inventory/include/include_refrigeratorListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId}});
	}
</script>

