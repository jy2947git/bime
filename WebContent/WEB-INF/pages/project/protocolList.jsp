<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="protocolList.title"/></title>
    <meta name="heading" content="<fmt:message key='protocolList.heading'/>"/>
    <meta name="menu" content="LabMenu"/>
</head>

<div align="right">
<a href="<c:url value='/project/protocol/0/form.html'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="protocol.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_protocolListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
function deleteChemicalShelves(itemId){
	new Ajax.Updater('myListDiv','<c:url value='/inventory/protocol/'/>' + itemId + '/delete.html?ajax=true',{method:'post'});
}
</script>
