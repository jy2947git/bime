<%@ include file="/common/taglibs.jsp" %>
<%@ page import="com.focaplo.myfuse.model.*" %>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder"%>

    <div id="divider"><div></div></div>
    <span class="left"><fmt:message key="webapp.version"/> |

        <c:if test="${pageContext.request.remoteUser != null}">
		<c:set var="authentication" value="${sessionScope['ACEGI_SECURITY_CONTEXT'].authentication}"/>
		
        | <fmt:message key="user.status"/> <security:authentication property="principal.fullName"/> 
        </c:if>
    </span>
    <span class="right">
        &copy; <fmt:message key="copyright.year"/> <a href="<fmt:message key="company.url"/>"><fmt:message key="company.name"/></a>
    </span>
