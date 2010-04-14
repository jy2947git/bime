<%@ include file="/common/taglibs.jsp"%>

<display:table name="workLogList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="workLog" pagesize="25" class="table" export="false">

    <display:column escapeXml="false" sortable="false" titleKey="history" style="width: 100%">
    	<fmt:message key="work.log.display"> 
    	<fmt:param value="${workLog.lastUpdateDate}"/>
    	<fmt:param value="${workLog.updatedByUser}"/>
    	<fmt:param value="${workLog.message}"/>
    	</fmt:message>
    </display:column>
   
    
    <display:setProperty name="paging.banner.item_name" value="comment"/>
    <display:setProperty name="paging.banner.items_name" value="comments"/>

</display:table>


