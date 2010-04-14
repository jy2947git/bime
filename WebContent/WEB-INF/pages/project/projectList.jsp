<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="projectList.title"/></title>
    <meta name="heading" content="<fmt:message key='projectList.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
</head>
<div align="right">
<a href="<c:url value='/project/projectForm.html?from=list&method=add'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="project.add"/></a>
</div>
<div id="projectListDiv">
<jsp:include page="include/include_projectListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteProject(itemId){
		new Ajax.Updater('projectListDiv','<c:url value='/project/include/include_projectListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId}});
	}
</script>


