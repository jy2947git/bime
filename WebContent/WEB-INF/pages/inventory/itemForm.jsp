<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="item.title"/></title>
    <meta name="heading" content="<fmt:message key='item.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<spring:bind path="managedItemWrapper.manaegdItem.*">
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

<form:form commandName="managedItemWrapper" method="post"  onsubmit="return onFormSubmit(this)" id="managedItemWrapperForm">
<form:hidden path="managedItem.id"/>
<form:hidden path="managedItem.version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>


<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

            <input type="button" class="button" name="cancel" onclick="parent.location='<c:url value="/inventory/items/list.html"/>'" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>

    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="itemCategory.name"/>
            <form:errors path="itemCategoryId" cssClass="fieldError"/>
            <form:select path="itemCategoryId">
            <form:options items="${itemCategoryList}" itemValue="id" itemLabel="name"/>
            <form:option value="" label="-----please select -----"/>
        	</form:select>
        	
            
            
        </div>
		<div>
		<div>
            <appfuse:label styleClass="desc" key="item.stored.amount"/>
            <form:errors path="managedItem.amount" cssClass="fieldError"/>
            <form:input path="managedItem.amount" id="managedItem.amount" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
        </div>
    </li>


   <li>
		<div class="left">
			<appfuse:label styleClass="desc" key="item.storage"/>
			<form:select path="managedItem.storigibleUniqueId">
            
            <form:options items="${allStoragableList}" itemValue="storigibleUniqueId" itemLabel="alias"/>
            <form:option value="" label="-----please select -----"/>
        	</form:select>
      </div>
      <div>
        	<appfuse:label styleClass="desc" key="item.storage.notes"/>
        	<form:input path="managedItem.storageNotes" id="managedItem.storageNotes" cssClass="text medium" cssErrorClass="text medium error" maxlength="20"/>
        </div>
    </li>
    <li>
    	<div>
    	<appfuse:label styleClass="desc" key="item.inventory.audit.notes"/>
    	<textarea name="notes" style="text large" rows="5" cols="40"></textarea>
    	</div>
    </li>
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>
<h2><fmt:message key="item.history.records"/></h2>
<display:table name="managedItemWrapper.managedItem.inventoryAudits" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="record" pagesize="25" class="table" export="false">
    <display:column property="createdDate" escapeXml="false" sortable="false" titleKey="item.history.date" format="{0,date,MM/dd/yyyy}" style="width: 20%"/>
    <display:column property="message" escapeXml="true" sortable="false" titleKey="item.history.notes" style="width: 80%"/>
</display:table>

<script type="text/javascript">

function onFormSubmit(theForm) {
    return validateManagedItemWrapper(theForm);
}
</script>

<v:javascript formName="managedItemWrapper" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

