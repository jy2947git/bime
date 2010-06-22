<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="meeting.title"/></title>
    <meta name="heading" content="<fmt:message key='meeting.heading'/>"/>
    <meta name="menu" content="LabMenu"/>
</head>
<div align="right">
<a href="<c:url value='/lab/meeting/0/form.html'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="meeting.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_meetingListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteMeeting(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/lab/meeting/'/>'+itemId+'/delete.html?ajax=true',{method:'post'});
	}
</script>


