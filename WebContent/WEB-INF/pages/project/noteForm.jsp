<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="note.title"/></title>
    <meta name="heading" content="<fmt:message key='note.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<spring:bind path="experimentNote.*">
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

<form:form commandName="experimentNote" method="post" onsubmit="return onFormSubmit(this)" id="experimentNoteForm">
<form:hidden path="id"/>
<form:hidden path="version"/>



<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

            <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>

    <li>
		<div class="left">
			<appfuse:label styleClass="desc" key="note.protocol"/>
			<form:select path="experimentProtocol">
            <option value="">------please select-----</option>
            
            <form:options items="${protocolList}" itemLabel="fullName"/>
            
        	</form:select>
      	</div>
      	<div>
			<appfuse:label styleClass="desc" key="note.project"/>
			<form:select path="managedProject">
			<option value="">------please select-----</option>
			<form:options items="${projectList}" itemLabel="name"/>
           
        	</form:select>
      	</div>
    </li>
    <li>
        <div>
            <appfuse:label styleClass="desc" key="note.notes"/>
            <form:errors path="notes" cssClass="fieldError"/>
     
            <form:textarea path="notes" id="notes" cssClass="text large" cssErrorClass="text medium error" rows="5" cols="40"/>
        </div>
        <div>
        </div>
	</li>
   
    
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>

<c:if test="${experimentNote.id!=null}">

<div id="myListDiv">
</div>
<div class="separator"></div>
<jsp:include page="include/include_image_upload.jsp"></jsp:include>
</c:if>
<script type="text/javascript">
new Ajax.Updater('myListDiv','<c:url value='/project/${projectId}/note/${noteId}'/>' + '/images/list.html?ajax=true',{method:'get'});


function onFormSubmit(theForm) {

    selectAll('accessUserIds');

    return validateExperimentNote(theForm);
}
function deleteExperimentImageFile(itemId){
	new Ajax.Updater('myListDiv','<c:url value='/project/${projectId}/note/${noteId}'/>/image/' + itemId + '/delete.html?ajax=true',{method:'post'});
}
</script>

<v:javascript formName="experimentNote" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

