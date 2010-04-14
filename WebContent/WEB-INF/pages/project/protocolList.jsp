<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="protocolList.title"/></title>
    <meta name="heading" content="<fmt:message key='protocolList.heading'/>"/>
    <meta name="menu" content="LabMenu"/>
</head>

<div align="right">
<a href="<c:url value='/project/protocolForm.html?method=Add&from=list'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="protocol.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_protocolListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteProtocol(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/project/include/include_protocolListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId}});
	}
</script>
