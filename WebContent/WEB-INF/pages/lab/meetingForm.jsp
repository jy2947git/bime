<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="meeting.title"/></title>
    <meta name="heading" content="<fmt:message key='meeting'/>"/>
    <meta name="menu" content="LabMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
    
  <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
  <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
  <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.7.2/jquery-ui.min.js"></script>

  <script type="text/javascript">
  jQuery.noConflict(); 
  
  jQuery(document).ready(function(){
	  jQuery("#startDate").datepicker({showOn:'button',buttonImageOnly:true,buttonImage:'<c:url value='/images/calendar_1.png'/>'});
	  jQuery("#endDate").datepicker({showOn:'button',buttonImageOnly:true,buttonImage:'<c:url value='/images/calendar_1.png'/>'});
  });
  </script>
</head>


<spring:bind path="labMeeting.*">
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

<form:form commandName="labMeeting" method="post" action="meetingForm.html" onsubmit="return onFormSubmit(this)" id="labMeetingForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>
<div align="right">
<a href="#" onclick="window.open('<c:url value="/lab/include/include_meetingPrint.html"/>')"><img src="<c:url value='/images/printer.png'/>"/><fmt:message key="meeting.print"/></a>
</div>

<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

            <input type="button" class="button" name="cancel" onclick="top.location='<c:url value="/lab/meetingList.html"/>'" value="<fmt:message key="button.cancel"/>"/>
            
        </c:set>
       
    </li>
    
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="meeting.subject"/>
            <form:errors path="subject" cssClass="fieldError"/>
            <form:input path="subject" id="subject" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
		<div>
			<appfuse:label styleClass="desc" key="meeting.coordinator"/>
			<form:select path="coordinator">
            <option value="">------please select-----</option>
            <form:options items="${userList}" itemLabel="fullName"/>
        	</form:select>
      	</div>
    </li>

     <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="meeting.startDate"/>
            <form:errors path="startDate" cssClass="fieldError"/>
            <form:input path="startDate" id="startDate" cssClass="text small-plus" cssErrorClass="text medium error" maxlength="20"/>
            <form:select path="startHourAndMinute">
            <form:options items="${hourAndMinuteLabelList}" itemLabel="label" itemValue="value"/>
        	</form:select>
            
        </div>
        <div>
            <appfuse:label styleClass="desc" key="meeting.endDate"/>
            <form:errors path="endDate" cssClass="fieldError"/>
            <form:input path="endDate" id="endDate" cssClass="text small-plus" cssErrorClass="text medium error" maxlength="20"/>
            <form:select path="endHourAndMinute">
            <form:options items="${hourAndMinuteLabelList}" itemLabel="label" itemValue="value"/>
        	</form:select>
        </div>
     </li>
     <li>
             <fieldset class="pickList">
            <legend><fmt:message key="meeting.participants"/></legend>
            <table class="pickList">
                <tr>
                    <th class="pickLabel">
                        <appfuse:label key="user.users" colon="false" styleClass="required"/>
                    </th>
                    <td></td>
                    <th class="pickLabel">
                        <appfuse:label key="meeting.participants" colon="false" styleClass="required"/>
                    </th>
                </tr>
                <c:set var="leftList" value="${userLabelList}" scope="request"/>
                <c:set var="rightList" value="${labMeeting.participantsLabelList}" scope="request"/>

                <c:import url="/WEB-INF/pages/pickList.jsp">
                    <c:param name="listCount" value="1"/>
                    <c:param name="leftId" value="userLabelList"/>
                    <c:param name="rightId" value="accessUserIds"/>
                </c:import>
            </table>
        </fieldset>
   </li>
     <li>
     	<div>
     	<appfuse:label styleClass="desc" key="meeting.description"/>
     	<form:textarea path="message" id="message" cssClass="text large" cssErrorClass="text medium error" rows="5" cols="40"/>
     	</div>
     </li>
   <li>
 

    
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>


<script type="text/javascript">
function onFormSubmit(theForm) {
	selectAll('accessUserIds');
    return validateLabMeeting(theForm);
}
</script>

<v:javascript formName="labMeeting" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

