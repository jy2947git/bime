<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="noteList.title"/></title>
    <meta name="heading" content="<fmt:message key='noteList.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
</head>


<div align="right">
<a href="<c:url value='/project/${projectId}/note/0/form.html'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="notes.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_noteListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteNote(itemId){
		new Ajax.Updater('myListDiv','/project/${projectId}/note/'+itemId+'/delete.html?ajax=true',{method:'post'});
	}
</script>


