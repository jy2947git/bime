<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="itemCategory.title"/></title>
    <meta name="heading" content="<fmt:message key='itemCategory.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<spring:bind path="itemCategory.*">
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

<form:form commandName="itemCategory" method="post" onsubmit="return onFormSubmit(this)" id="itemCategoryForm">
<form:hidden path="id"/>
<form:hidden path="version"/>



<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

           <input type="button" class="button" name="cancel" onclick="parent.location='<c:url value="/inventory/itemCategories/list.html"/>'" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>

    
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="itemCategory.name"/>
            <form:errors path="name" cssClass="fieldError"/>
            <form:input path="name" id="name" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
        <div>
            <appfuse:label styleClass="desc" key="itemCategory.type"/>
            <form:errors path="type" cssClass="fieldError"/>
            <form:input path="type" id="type" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
    </li>
	<li>
        <div class="left">
            <appfuse:label styleClass="desc" key="itemCategory.inventory.threshold"/>
            <form:errors path="inventoryThreshold" cssClass="fieldError"/>
            <form:input path="inventoryThreshold" id="inventoryThreshold" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
        <div>
            &nbsp;
        </div>
    </li>
   
    
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>

<script type="text/javascript">

function onFormSubmit(theForm) {
    return validateItemCategory(theForm);
}
</script>

<v:javascript formName="itemCategory" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

