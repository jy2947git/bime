<%@ include file="/common/taglibs.jsp"%>

<display:table name="experimentProtocolList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="protocol" pagesize="25" class="table" export="false">

    <display:column property="fullName" escapeXml="true" sortable="false" titleKey="protocol.name" style="width: 30%"/>
    
    <display:column property="createdByName" escapeXml="true" sortable="false" titleKey="protocol.created_by" style="width: 30%"/>
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/project/protocol/${protocol.id}/form.html'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="protocol.edit"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="protocol"/></fmt:message>')){deleteProtocol('${protocol.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="protocol.delete"/>'/></a>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="protocol"/>
    <display:setProperty name="paging.banner.items_name" value="protocols"/>

</display:table>