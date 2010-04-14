<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="workLog.title"/></title>
    <meta name="heading" content="<fmt:message key='workLog.heading'/>"/>
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
	
  </script>
	 
</head>

 <%@ include file="/common/messages.jsp" %>
<spring:bind path="workLog.*">
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

<form:form commandName="workLog" method="post" action="include_workLogForm.html" onsubmit="return onFormSubmit(this)" id="workLogForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>
<input type="hidden" name="toDoId" value="<c:out value="${workLog.toDo.id}"/>"/>

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
                <p><fmt:message key="workLog.update.message"/></p>
            </c:when>
            <c:otherwise>
                <p><fmt:message key="workLog.add.message"/></p>
            </c:otherwise>
        </c:choose>
    </li>


        <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="workLog.comment"/>
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
    Form.focusFirstElement($('workLogForm'));
    highlightFormElements();

  

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {


    return validateWorkLog(theForm);
}


</script>


        
        
<v:javascript formName="workLogForm" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

