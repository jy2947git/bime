<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="project.title"/></title>
    <meta name="heading" content="<fmt:message key='project.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
    
  <link href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css" rel="stylesheet" type="text/css"/>
  <script src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js"></script>

  <script type="text/javascript">
  jQuery.noConflict(); 
  
  jQuery(document).ready(function(){
	  jQuery("#startDate").datepicker({showOn:'button',buttonImageOnly:true,buttonImage:'<c:url value='/images/calendar_1.png'/>'});
	  jQuery("#endDate").datepicker({showOn:'button',buttonImageOnly:true,buttonImage:'<c:url value='/images/calendar_1.png'/>'});
  });
  </script>
</head>


<spring:bind path="managedProject.*">
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

<form:form commandName="managedProject" method="post" action="projectForm.html" onsubmit="return onFormSubmit(this)" id="managedProjectForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>


<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
        <c:if test="${(managedProject!=null &&  managedProject.id==null) || (managedProject!=null &&  managedProject.id!=null && managedProject.canAuthorize)}">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>
		</c:if>
            <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>
    
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="project.name"/>
            <form:errors path="name" cssClass="fieldError"/>
            <form:input path="name" id="name" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
		<div>
			<appfuse:label styleClass="desc" key="project.principal"/>
			<form:select path="principal">
            <option value="">------please select-----</option>
            <form:options items="${userList}" itemLabel="fullName"/>
        	</form:select>
      	</div>
    </li>
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="project.fund_source"/>
            <form:errors path="fundSource" cssClass="fieldError"/>
            <form:input path="fundSource" id="fundSource" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
        <div>
        	<appfuse:label styleClass="desc" key="project.status"/>
			<form:select path="status">
            <option value="">------please select-----</option>
            
            <form:options items="${projectStatusLabelList}" itemValue="value" itemLabel="label"/>
            
        	</form:select>
        </div>
     </li>
     <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="project.start_date"/>
            <form:errors path="startDate" cssClass="fieldError"/>
            <form:input path="startDate" id="startDate" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
        <div>
            <appfuse:label styleClass="desc" key="project.end_date"/>
            <form:errors path="endDate" cssClass="fieldError"/>
            <form:input path="endDate" id="endDate" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
     </li>
     <li>
     	<div>
     	<appfuse:label styleClass="desc" key="project.description"/>
     	<form:textarea path="description" id="description" cssClass="text large" cssErrorClass="text medium error" rows="5" cols="40"/>
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

    return validateManagedProject(theForm);
}
</script>

<v:javascript formName="managedProject" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

