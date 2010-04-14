<%@ include file="/common/taglibs.jsp"%>

<display:table name="labMeetingItemList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="meetingItem" pagesize="25" class="table" export="false">

	<display:column property="speaker.fullName" escapeXml="false" sortable="false" titleKey="meeting.speaker" style="width: 20%"/> 
	

    <display:column property="topic" escapeXml="true" sortable="false" titleKey="meeting.topic" style="width: 60%"/>
    
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/lab/meetingItemForm.html?from=list&id=${meetingItem.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="meeting.edit"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="topic"/></fmt:message>')){deleteMeetingItem('${meetingItem.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="meeting.delete"/>'/></a>
    </display:column>
    
    <display:setProperty name="paging.banner.item_name" value="topic"/>
    <display:setProperty name="paging.banner.items_name" value="topics"/>

</display:table>