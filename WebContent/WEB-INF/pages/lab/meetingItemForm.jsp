<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="meeting.title"/></title>
    <meta name="heading" content="<fmt:message key='meeting'/>"/>
    <meta name="menu" content="LabMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
    
</head>


<spring:bind path="labMeetingItem.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>

<form:form commandName="labMeetingItem" method="post" action="meetingItemForm.html" onsubmit="return onFormSubmit(this)" id="labMeetingItemForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>


<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

            <input type="button" class="button" name="cancel" onclick="top.location='<c:url value="/lab/meetingItemList.html"/>'" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>
    
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="meeting.topic"/>
            <form:errors path="topic" cssClass="fieldError"/>
            <form:input path="topic" id="topic" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
		<div>
			<appfuse:label styleClass="desc" key="meeting.speaker"/>
			<form:select path="speaker">
            <option value="">------please select-----</option>
            <form:options items="${userList}" itemLabel="fullName"/>
        	</form:select>
      	</div>
    </li>
 

    
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>


<script type="text/javascript">
function onFormSubmit(theForm) {
    return validateLabMeetingItem(theForm);
}
</script>

<v:javascript formName="labMeetingItem" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

