<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="project.access.title"/></title>
    <meta name="heading" content="<fmt:message key='project.access.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
    
</head>


<spring:bind path="managedProject.*">
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

<form:form commandName="managedProject" method="post"  onsubmit="return onFormSubmit(this)" id="projectAccessForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>


<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
        <c:if test="${(managedProject!=null &&  managedProject.id==null) || (managedProject!=null &&  managedProject.id!=null && managedProject.canAuthorize)}">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>
		</c:if>
			 <input type="button" class="button" name="cancel" onclick="parent.location='<c:url value="/projects/list.html"/>'" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>
    <li class="info">
        <c:choose>
            <c:when test="${param.from == 'list'}">
                <p><fmt:message key="project.update.message"/></p>
            </c:when>
            <c:otherwise>
                <p><fmt:message key="project.add.message"/></p>
            </c:otherwise>
        </c:choose>
    </li>
    
  
   <li>
 
        <fieldset class="pickList">
            <legend><fmt:message key="project.participantsist"/></legend>
            <table class="pickList">
                <tr>
                    <th class="pickLabel">
                        <appfuse:label key="user.users" colon="false" styleClass="required"/>
                    </th>
                    <td></td>
                    <th class="pickLabel">
                        <appfuse:label key="user.accessed" colon="false" styleClass="required"/>
                    </th>
                </tr>
                <c:set var="leftList" value="${userLabelList}" scope="request"/>
                <c:set var="rightList" value="${managedProject.participantsLabelList}" scope="request"/>

                <c:import url="/WEB-INF/pages/pickList.jsp">
                    <c:param name="listCount" value="1"/>
                    <c:param name="leftId" value="userLabelList"/>
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
<script type="text/javascript">

function onFormSubmit(theForm) {
	selectAll('accessUserIds');
}
</script>
