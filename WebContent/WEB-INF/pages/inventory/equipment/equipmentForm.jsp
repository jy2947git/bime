<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="equipment.title"/></title>
    <meta name="heading" content="<fmt:message key='equipment.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<spring:bind path="equipment.*">
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

<form:form commandName="equipment" method="post" action="equipmentForm.html" onsubmit="return onFormSubmit(this)" id="equipmentForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>


<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

        <c:if test="${param.from == 'list' and param.method != 'Add'}">
            <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('equipment')"
                value="<fmt:message key="button.delete"/>"/>
        </c:if>

            <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>
    <li class="info">
        <c:choose>
            <c:when test="${param.from == 'list'}">
                <p><fmt:message key="equipment.update.message"/></p>
            </c:when>
            <c:otherwise>
                <p><fmt:message key="equipment.add.message"/></p>
            </c:otherwise>
        </c:choose>
    </li>
    
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="equipment.name"/>
            <form:errors path="name" cssClass="fieldError"/>
            <form:input path="name" id="name" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
        <div>
            <appfuse:label styleClass="desc" key="equipment.type"/>
            <form:errors path="type" cssClass="fieldError"/>
            <form:input path="type" id="type" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
    </li>
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="equipment.equiptmentCondition"/>
                <form:errors path="equiptmentCondition" cssClass="fieldError"/>
                <form:input path="equiptmentCondition" id="equiptmentCondition" cssClass="text medium" cssErrorClass="text medium error"/>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="equipment.location"/>
                <form:errors path="location" cssClass="fieldError"/>
                <form:input path="location" id="location" cssClass="text medium" cssErrorClass="text medium error"/>
            </div>
        </div>
    </li>
   
    
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>

<script type="text/javascript">
    Form.focusFirstElement($('equipmentForm'));
    highlightFormElements();

  

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
    return validateEquipment(theForm);
}
</script>

<v:javascript formName="equipment" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

