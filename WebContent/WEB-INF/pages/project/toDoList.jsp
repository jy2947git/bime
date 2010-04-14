<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="toDo.title"/></title>
    <meta name="heading" content="<fmt:message key='toDo.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
</head>


<div align="right">
<a href="<c:url value='/project/toDoForm.html?from=list&method=add'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="toDo.add"/></a>
</div>
<div id="toDoListDiv">
<jsp:include page="include/include_toDoListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteToDo(itemId){
		new Ajax.Updater('toDoListDiv','<c:url value='/project/include/include_toDoListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId}});
	}
</script>
