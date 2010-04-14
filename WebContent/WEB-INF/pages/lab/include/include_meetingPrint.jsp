<%@ include file="/common/taglibs.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
    <head>
    <link rel="stylesheet" type="text/css" href="<c:url value='/styles/${appConfig["csstheme"]}/meeting-print.css'/>" />
    </head>


<body>
<table width="500" border="0" cellspacing="5" class="meeting-table">
  <tr>
    <td colspan="2" class="meeting-subject"><c:out value="${labMeeting.subject}"/></td>
  </tr>
  <tr>
    <td class="meeting-column-left"><fmt:message key="meeting.agenda"/></td>
    <td class="meeting-column-right"><c:out value="${labMeeting.message}"/></td>
  </tr>
  <tr>
    <td class="meeting-column-left"><fmt:message key="meeting.time"/></td>
    <td class="meeting-column-right"><fmt:formatDate value="${labMeeting.startDate}" type="BOTH" dateStyle="short" timeStyle="short"/> - <fmt:formatDate value="${labMeeting.endDate}" type="BOTH" dateStyle="short" timeStyle="short"/></td>
  </tr>
  <tr>
    <td class="meeting-column-left"><fmt:message key="meeting.coordinator"/></td>
    <td class="meeting-column-right"><c:out value="${labMeeting.coordinator.fullName}"/></td>
  </tr>
  <tr>
    <td class="meeting-column-left"><fmt:message key="meeting.participants"/></td>
    <td class="meeting-column-right">
    <c:forEach var="usr" items="${labMeeting.participants}">
		<c:out value="${usr.fullName}"/>&nbsp;
	</c:forEach></td>
  </tr>
  <tr>
    <td class="meeting-column-left"><div align="center" class="meeting-speaker"><fmt:message key="meeting.speaker"/></div></td>
    <td><div align="center" class="meeting-topic"><fmt:message key="meeting.topic"/></div></td>
  </tr>
  <c:forEach var="topic" items="${labMeeting.meetingItems}">
  	<tr>
		<td  class="meeting-column-left"><c:out value="${topic.speaker.fullName}"/></td>
		<td class="meeting-column-right"><c:out value="${topic.topic}"/></td>
	</tr>
	</c:forEach>

</table>
</body>
</html>