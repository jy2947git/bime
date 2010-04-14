<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="meeting.title"/></title>
    <meta name="heading" content="<fmt:message key='meeting.heading'/>"/>
    <meta name="menu" content="LabMenu"/>
</head>
<div align="right">
<a href="<c:url value='/lab/meetingItemForm.html?method=Add&from=list'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="meeting.addTopic"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_meetingItemListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteMeetingItem(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/lab/include/include_meetingItemListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId}});
	}
</script>


