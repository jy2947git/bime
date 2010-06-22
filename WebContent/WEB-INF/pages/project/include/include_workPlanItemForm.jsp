<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="workplan.item.title"/></title>
    <meta name="heading" content="<fmt:message key='workplan.item.heading'/>"/>
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
<spring:bind path="workPlanItem.*">
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

<form:form commandName="workPlanItem" method="post" action="include_workPlanItemForm.html" onsubmit="return onFormSubmit(this)" id="workPlanItemForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>
<input type="hidden" name="planId" value="<c:out value="${planId}"/>"/>

<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

        </c:set>
       
    </li>
    <li class="info">
        <c:choose>
            <c:when test="${param.id != null}">
                <p><fmt:message key="workplan.item.update.message"/></p>
            </c:when>
            <c:otherwise>
                <p><fmt:message key="workplan.item.add.message"/></p>
            </c:otherwise>
        </c:choose>
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
			<appfuse:label styleClass="desc" key="workplan.status"/>
			<form:select path="status">
            <option value="">------please select-----</option>
            
            <form:options items="${statusLabelList}" itemValue="value" itemLabel="label"/>
            
        	</form:select>
      	</div>
    </li>
    <li>
      	<div  class="left">
			<appfuse:label styleClass="desc" key="workPlanItem.subject"/>
			<form:errors path="subject" cssClass="fieldError"/>
            <form:input path="subject" id="subject" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
      	</div>
    </li>
        <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="workplan.item.notes"/>
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
    Form.focusFirstElement($('workPlanItemForm'));
    highlightFormElements();

  

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {


    return validateWorkPlanItem(theForm);
}


</script>


        
        
<v:javascript formName="workPlanItem" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

