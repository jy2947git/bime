<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="heading" content="<fmt:message key='userList.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/lab/user/0/form.html"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="button" onclick="location.href='<c:url value="/mainMenu.html"/>'"
        value="<fmt:message key="button.done"/>"/>
</c:set>



<display:table name="userList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="myuser" pagesize="25" class="table" export="false">
    <display:column media="html" style="width: 25%">
        <a href="<c:url value='/lab/user/${myuser.id}/form.html'/>"><c:out value="${myuser.username}"/></a>
    </display:column>
    <display:column property="fullName" escapeXml="true" sortable="true" titleKey="activeUsers.fullName" style="width: 34%"/>
    <display:column property="email" sortable="true" titleKey="user.email" style="width: 25%" autolink="true" media="html"/>
    <display:column property="email" titleKey="user.email" media="csv xml excel pdf"/>
    <display:column sortProperty="enabled" sortable="true" titleKey="user.enabled" style="width: 16%; padding-left: 15px" media="html">
        <input type="checkbox" disabled="disabled" <c:if test="${myuser.enabled}">checked="checked"</c:if>/>
    </display:column>

    <display:setProperty name="paging.banner.item_name" value="user"/>
    <display:setProperty name="paging.banner.items_name" value="users"/>

</display:table>

<c:out value="${buttons}" escapeXml="false" />

<script type="text/javascript">
    highlightTableRows("users");
</script>
