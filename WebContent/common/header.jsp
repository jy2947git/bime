<%@ include file="/common/taglibs.jsp"%>

<c:if test="${pageContext.request.locale.language != 'en'}">
    <div id="switchLocale"><a href="<c:url value='/?locale=en'/>"><fmt:message key="webapp.name"/> in English</a></div>
</c:if>

<c:if test="${pageContext.request.remoteUser != null}">
<span class="right">

			<a href="<c:url value='/lab/user/'/><security:authentication property="principal.id"/>/form.html"><fmt:message key="my.profile"/></a>
			|
			<a href="<c:url value="/logout.jsp"/>"> <fmt:message key="log.out"/></a>
		

</span>
</c:if>
<div id="branding">
    <h1><a href="<c:url value='/'/>"><fmt:message key="webapp.name"/></a></h1>
    <p><fmt:message key="webapp.tagline"/></p>
</div>



<hr />

<%-- Put constants into request scope --%>
<appfuse:constants scope="request"/>