<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="refrigerator.title"/></title>
    <meta name="heading" content="<fmt:message key='refrigerator.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
    <script type='text/javascript' src='<c:url value='/dwr/engine.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/dwr/util.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/scripts/order.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/dwr/interface/InventoryManager.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/scripts/storage.js'/>'></script>
</head>

<spring:bind path="refrigerator.*">
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



<form:form commandName="refrigerator" method="post" action="refrigeratorForm.html" onsubmit="return onFormSubmit(this)" id="refrigeratorForm">
<form:hidden path="id"/>
<form:hidden path="version"/>


<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

            <input type="button" class="button" name="cancel" onclick="parent.location='<c:url value="/inventory/refrigeratorList.html"/>'" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>

    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="refrigerator.name"/>
            <form:errors path="name" cssClass="fieldError"/>
            <form:input path="name" id="name" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
		<div>
            <appfuse:label styleClass="desc" key="refrigerator.type"/>
            <form:errors path="type" cssClass="fieldError"/>
            <form:input path="type" id="type" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
    </li>
    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="refrigerator.location"/>
                <form:errors path="location" cssClass="fieldError"/>
                <form:input path="location" id="location" cssClass="text medium" cssErrorClass="text medium error"/>
            </div>
            <div>
                <appfuse:label styleClass="desc" key="refrigerator.contactPerson"/>
                <form:errors path="contactPersion" cssClass="fieldError"/>
                <form:input path="contactPersion" id="contactPersion" cssClass="text medium" cssErrorClass="text medium error"/>
            </div>
        </div>
    </li>

    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>

<c:choose>
<c:when test="${refrigerator.id!=null}">
<jsp:include page="include/include_storage_sections.jsp"></jsp:include>
<script type="text/javascript">
	var storageId = <c:out value="${refrigerator.id}"/>;
	init();
</script>
</c:when>
</c:choose>
<script type="text/javascript">

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
    return validateRefrigerator(theForm);
}
</script>

<v:javascript formName="refrigerator" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

