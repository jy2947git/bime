<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="toDo.title"/></title>
    <meta name="heading" content="<fmt:message key='toDo.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
</head>


<div align="right">
<a href="<c:url value='/project/${projectId}/todo/0/form.html?ajax=true'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="toDo.add"/></a>
</div>
<div id="toDoListDiv">
<jsp:include page="include/include_toDoListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteToDo(itemId){
		new Ajax.Updater('toDoListDiv','<c:url value='/project/${projectId}/todo/'/>'+itemId+'/delete.html?ajax=true',{method:'post'});
	}
</script>
