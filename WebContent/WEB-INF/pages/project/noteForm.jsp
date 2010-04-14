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

<form:form commandName="experimentNote" method="post" action="noteForm.html" onsubmit="return onFormSubmit(this)" id="experimentNoteForm">
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
      	<div class="right">
			<appfuse:label styleClass="desc" key="note.project"/>
			<form:select path="managedProject">
			<option value="">------please select-----</option>
			<form:options items="${projectList}" itemLabel="name"/>
           
        	</form:select>
      	</div>
    </li>
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="note.notes"/>
            <form:errors path="notes" cssClass="fieldError"/>
     
            <form:textarea path="notes" id="notes" cssClass="text large" cssErrorClass="text medium error" rows="5" cols="40"/>
        </div>
	</li>
    <li>
 
        <fieldset class="pickList">
            <legend><fmt:message key="notes.accessList"/></legend>
            <table class="pickList">
                <tr>
                    <th class="pickLabel">
                        <appfuse:label key="user.users" colon="false" styleClass="required"/>
                    </th>
                    <td></td>
                    <th class="pickLabel">
                        <appfuse:label key="notes.accessList" colon="false" styleClass="required"/>
                    </th>
                </tr>
                <c:set var="leftList" value="${userList}" scope="request"/>
                <c:set var="rightList" value="${experimentNote.accessedUsers}" scope="request"/>

                <c:import url="/WEB-INF/pages/pickList.jsp">
                    <c:param name="listCount" value="1"/>
                    <c:param name="leftId" value="userList"/>
                    <c:param name="rightId" value="accessUserIds"/>
                </c:import>
            </table>
        </fieldset>
   </li>
    
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>

<c:if test="${experimentNote.id!=null}">

<div id="myListDiv">
<jsp:include page="include/include_experimentImageListTable.jsp"></jsp:include>
</div>
<div class="separator"></div>
<jsp:include page="include/include_image_upload.jsp"></jsp:include>
</c:if>
<script type="text/javascript">

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {

    selectAll('accessUserIds');

    return validateExperimentNote(theForm);
}
function deleteExperimentImageFile(itemId){
	new Ajax.Updater('myListDiv','<c:url value='/project/include/include_experimentImageListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId,id:'<c:out value="${experimentNote.id}"/>'}});
}
</script>

<v:javascript formName="experimentNote" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

