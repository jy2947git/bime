<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="toDo.title"/></title>
    <meta name="heading" content="<fmt:message key='toDo.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
   
	<script type="text/javascript">
    var GB_ROOT_DIR = "<c:url value='/scripts/greybox/'/>";
	</script>
	<script type="text/javascript" src="<c:url value='/scripts/greybox/AJS.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/greybox/AJS_fx.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/greybox/gb_scripts.js'/>"></script>
	<link href="<c:url value='/scripts/greybox/gb_styles.css'/>" rel="stylesheet" type="text/css" />
	
	
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/theme.css'/>" />
   <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/styles/${appConfig["csstheme"]}/print.css'/>" />


	 
<link href="<c:url value='/styles/jquery-ui-1.8.1.custom.css'/>" rel="stylesheet" type="text/css"/>
      <script src="<c:url value='/scripts/jquery-ui.min.js'/>"></script>
  <script type="text/javascript">
  jQuery.noConflict(); 
  
  jQuery(document).ready(function(){
	  jQuery("#startDate").datepicker({showOn:'button',buttonImageOnly:true,buttonImage:'<c:url value='/images/calendar_1.png'/>'});
	  jQuery("#endDate").datepicker({showOn:'button',buttonImageOnly:true,buttonImage:'<c:url value='/images/calendar_1.png'/>'});
  });
  </script>
	 
</head>

 <%@ include file="/common/messages.jsp" %>
<spring:bind path="toDo.*">
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

<form:form commandName="toDo" method="post"  onsubmit="return onFormSubmit(this)" id="toDoForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>

<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

			<input type="button" class="button" name="cancel" onclick="parent.location='<c:url value="/projects/list.html"/>'" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
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
      	<div  class="left">
			<appfuse:label styleClass="desc" key="toDo.status"/>
			<form:select path="status">
            <option value="">------please select-----</option>
            
            <form:options items="${statusLabelList}" itemValue="value" itemLabel="label"/>
            
        	</form:select>
      	</div>
    </li>
    <li>
      	<div  class="left">
			<appfuse:label styleClass="desc" key="toDo.subject"/>
			<form:errors path="subject" cssClass="fieldError"/>
            <form:input path="subject" id="subject" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
      	</div>
    </li>
        <li>
        <div>
            <appfuse:label styleClass="desc" key="toDo.notes"/>
            <form:errors path="message" cssClass="fieldError"/>
     
            <form:textarea path="message" id="message" cssClass="text large" cssErrorClass="text medium error" rows="5" cols="40"/>
        </div>
	</li>
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>

</ul>

</form:form>

              
<script type="text/javascript">
function onFormSubmit(theForm) {
    return validateToDo(theForm);
}


</script>


        
<c:if test="${toDo.id != null}">

<div align="right">
<a href="#" onclick="return GB_showCenter('comment','<c:url value='/project/${projectId}/todo/${toDo.id}/worklog/0/form.html'/>',500,500,displayWorkLogs)"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="button.add_work_log"/></a>
</div>
<div id="myLogs"></div>
<script type="text/javascript">
	function displayWorkLogs(){
		
		new Ajax.Updater('myLogs','<c:url value='/project/${projectId}/todo/${toDo.id}/worklogs/list.html?ajax=true'/>',{method:'get'});
	}
	displayWorkLogs();
</script>
</c:if>
<v:javascript formName="toDo" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

