<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="protocol.title"/></title>
    <meta name="heading" content="<fmt:message key='protocol.heading'/>"/>
    <meta name="menu" content="LabMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<spring:bind path="experimentProtocol.*">
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

<form:form commandName="experimentProtocol" method="post" action="protocolForm.html" onsubmit="return onFormSubmit(this)" id="experimentProtocolForm">
<form:hidden path="id"/>
<form:hidden path="version"/>

<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>
            <c:if test="${experimentProtocol.id != null}">
				<input type="submit" class="button" name="newVersion" onclick="bCancel=false" value="<fmt:message key="button.saveAsNewVersion"/>"/>
			</c:if>
            <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>
    <li class="info">
        <c:choose>
            <c:when test="${param.from == 'list'}">
                <p><fmt:message key="protocol.update.message"/></p>
            </c:when>
            <c:otherwise>
                <p><fmt:message key="protocol.add.message"/></p>
            </c:otherwise>
        </c:choose>
    </li>
    
    <li>
		<div class="left">
            <appfuse:label styleClass="desc" key="protocol.name"/>
            <form:errors path="name" cssClass="fieldError"/>
            <form:input path="name" id="name" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
		</div>
		<div>
			<appfuse:label styleClass="desc" key="protocol.version"/>
            <form:errors path="protocolVersion" cssClass="fieldError"/>
            <form:input path="protocolVersion" id="protocolVersion" readonly="true" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
         </div>
    </li>
    <li>

            <appfuse:label styleClass="desc" key="protocol.procedure"/>
            <form:errors path="experimentProcedure" cssClass="fieldError"/>
            <form:textarea path="experimentProcedure" cssClass="text large" cssErrorClass="text medium error" rows="5" cols="40"></form:textarea>
 
    </li>
   
    
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>
<h2><fmt:message key="protocol.history.records"/></h2>
<display:table name="experimentProtocol.protocolAudits" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="record" pagesize="25" class="table" export="false">
    <display:column property="createdDate" escapeXml="false" sortable="false" titleKey="protocol.history.date" format="{0,date,MM/dd/yyyy}" style="width: 20%"/>
    <display:column property="message" escapeXml="true" sortable="false" titleKey="protocol.history.notes" style="width: 80%"/>
</display:table>
<script type="text/javascript">

function onFormSubmit(theForm) {
    return validateExperimentProtocol(theForm);
}
</script>

<v:javascript formName="experimentProtocol" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

