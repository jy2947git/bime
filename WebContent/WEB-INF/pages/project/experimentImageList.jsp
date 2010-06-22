<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="note.title"/></title>
    <meta name="heading" content="<fmt:message key='note.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
</head>
<div id="myListDiv">
<jsp:include page="include/include_experimentImageListTable.jsp"></jsp:include>
</div>
<div class="separator"></div>
<div id="fileUploadDiv">
<jsp:include page="include/include_image_upload.jsp"></jsp:include>
</div>
<script type="text/javascript">
function deleteExperimentImageFile(itemId){
	new Ajax.Updater('myListDiv','<c:url value='/project/include/include_experimentImageListTable.html'/>',{method:'post', parameters:{from:'list',requestedMethod:'delete',selected:itemId,id:}});
}
</script>