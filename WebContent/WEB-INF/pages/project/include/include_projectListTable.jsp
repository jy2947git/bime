<%@ include file="/common/taglibs.jsp"%>

<display:table name="managedProjectList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="project" pagesize="25" class="table" export="false">

    <display:column property="name" escapeXml="true" sortable="false" titleKey="project.name" style="width: 20%"/>
    <display:column property="fundSource" escapeXml="true" sortable="false" titleKey="project.fund_source" style="width: 20%"/>
    <display:column property="principal.fullName" escapeXml="true" sortable="false" titleKey="project.principal" style="width: 20%"/>
    <display:column escapeXml="true" sortable="false" titleKey="project.status" style="width: 20%">
    	<fmt:message key="${project.status}"/>
	</display:column>
	 
    <display:column style="width: 16%; padding-left: 15px" media="html">
    <c:if test="${project.canEdit}">
    	<a href="<c:url value='/project/projectForm.html?from=list&id=${project.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="project.edit"/>"/></a>&nbsp;
    </c:if>
    &nbsp;
    <c:if test="${project.canDelete}">
    	<a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="project"/></fmt:message>')){deleteProject('${project.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="project.delete"/>'/></a>
    </c:if>
    &nbsp;

    <c:if test="${project.isLocked}">
    	<img src="<c:url value='/images/lock.png'/>" alt="<fmt:message key="locked"/>"/>
    </c:if>
         
    </display:column>
    
    
    <display:setProperty name="paging.banner.item_name" value="project"/>
    <display:setProperty name="paging.banner.items_name" value="projects"/>

</display:table>