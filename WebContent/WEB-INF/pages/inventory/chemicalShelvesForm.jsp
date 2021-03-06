<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="chemicalShelves.title"/></title>
    <meta name="heading" content="<fmt:message key='chemicalShelves.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>

    <script type='text/javascript' src='<c:url value='/scripts/storage_section_jquery.js'/>'></script>
</head>

<spring:bind path="chemicalShelves.*">
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

<form:form commandName="chemicalShelves" method="post" onsubmit="return onFormSubmit(this)" id="chemicalShelvesForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>


<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

            <input type="button" class="button" name="cancel" onclick="parent.location='<c:url value="/inventory/chemicalShelves/list.html"/>'" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>

    
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="chemicalShelves.name"/>
            <form:errors path="name" cssClass="fieldError"/>
            <form:input path="name" id="name" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
		<div>
            <appfuse:label styleClass="desc" key="chemicalShelves.type"/>
            <form:errors path="type" cssClass="fieldError"/>
            <form:input path="type" id="type" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
    </li>

    <li>
        <div>
            <div class="left">
                <appfuse:label styleClass="desc" key="chemicalShelves.location"/>
                <form:errors path="location" cssClass="fieldError"/>
                <form:input path="location" id="location" cssClass="text medium" cssErrorClass="text medium error"/>
            </div>
			<div>
				<appfuse:label styleClass="desc" key="chemicalShelves.contactPerson"/>
				<form:select path="contactPerson">
	            <option value="">------please select-----</option>
	            <form:options items="${userList}" itemLabel="fullName"/>
	        	</form:select>
      		</div>
        </div>
    </li>
   
    
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>
<c:choose>
<c:when test="${chemicalShelves.id!=null}">
<jsp:include page="include/include_storage_sections_2.jsp"></jsp:include>
<script type="text/javascript">
	var storageId = <c:out value="${chemicalShelves.id}"/>;
	getSections(storageId);
</script>
</c:when>
</c:choose>
<script type="text/javascript">

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
    return validateChemicalShelves(theForm);
}
</script>

<v:javascript formName="chemicalShelves" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

