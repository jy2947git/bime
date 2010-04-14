<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="grantList.title"/></title>
    <meta name="heading" content="<fmt:message key='grantList.heading'/>"/>
    <meta name="menu" content="GrantMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/grant/grantForm.html?method=Add&from=list"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="submit" style="margin-right: 5px"
        
        value="<fmt:message key="button.delete"/>"/>
</c:set>

<form name="grantListForm" method="post" action="grantListForm.html">
<input type="hidden" name="requestedMethod" value="delete"/>
<display:table name="grantList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="grant" pagesize="25" class="table" export="true">
    <display:column style="width: 16%; padding-left: 15px" media="html">
        <input type="checkbox" name="selected" value="<c:out value="${grant.id}"/>"/>
    </display:column>
    <display:column property="name" escapeXml="true" sortable="true" titleKey="grant.name" style="width: 20%"
        url="/grant/grantForm.html?from=list" paramId="id" paramProperty="id"/>
    

    <display:setProperty name="paging.banner.item_name" value="grant"/>
    <display:setProperty name="paging.banner.items_name" value="grants"/>

    <display:setProperty name="export.excel.filename" value="User List.xls"/>
    <display:setProperty name="export.csv.filename" value="User List.csv"/>
    <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
</display:table>

<c:out value="${buttons}" escapeXml="false" />
</form>
<script type="text/javascript">
    highlightTableRows("grants");
</script>
