<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="meeting.upload.title"/></title>
    <meta name="heading" content="<fmt:message key='meeting.upload.heading'/>"/>
    <meta name="menu" content="LabMenu"/>
</head>
<div id="myListDiv">
<jsp:include page="include/include_meetingFileListTable.jsp"></jsp:include>
</div>
<div class="separator"></div>
<div id="fileUploadDiv">
<jsp:include page="include/include_image_upload.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteMeetingFile(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/lab/meeting/${meetingId}/document/'/>' + itemId + '/delete.html?ajax=true',{method:'delete'});
	}
</script>