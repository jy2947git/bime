<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="chemicalShelvesList.title"/></title>
    <meta name="heading" content="<fmt:message key='chemicalShelvesList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>

<div align="right">
<a href="<c:url value='/inventory/chemicalShelvesForm.html?method=Add&from=list'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="chemicalShelves.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_chemicalShelvesListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteChemicalShelves(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/inventory/include/include_chemicalShelvesListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId}});
	}
</script>
