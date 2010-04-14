<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="noteList.title"/></title>
    <meta name="heading" content="<fmt:message key='noteList.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
</head>


<div align="right">
<a href="<c:url value='/project/noteForm.html?method=Add&from=list'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="notes.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_noteListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteNote(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/project/include/include_noteListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId}});
	}
</script>


