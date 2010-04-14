<%@ include file="/common/taglibs.jsp"%>

<display:table name="labMeetingList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="meeting" pagesize="25" class="table" export="false">

	<display:column property="startDate" escapeXml="false" sortable="false" titleKey="meeting.startDate"  format="{0,date,MM/dd/yyyy HH:mm}" style="width: 20%"/> 
	<display:column property="endDate" escapeXml="false" sortable="false" titleKey="meeting.startDate"  format="{0,date,MM/dd/yyyy HH:mm}" style="width: 20%"/>

    <display:column property="subject" escapeXml="true" sortable="false" titleKey="meeting.subject" style="width: 60%"/>
    
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/lab/meetingForm.html?from=list&id=${meeting.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="meeting.edit"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="meeting"/></fmt:message>')){deleteMeeting('${meeting.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="meeting.delete"/>'/></a>
    </display:column>
    
    <display:setProperty name="paging.banner.item_name" value="meeting"/>
    <display:setProperty name="paging.banner.items_name" value="meetings"/>

</display:table>