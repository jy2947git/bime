<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="meeting.upload.title"/></title>
    <meta name="heading" content="<fmt:message key='meeting.upload.heading'/>"/>
    <meta name="menu" content="LabMenu"/>
</head>
<display:table name="meetingFileList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="image" pagesize="25" class="table" export="false">

    <display:column property="fileName" escapeXml="true" sortable="false" titleKey="note.file.name" style="width: 50%"/>
    <display:column property="uploadedBy.fullName" escapeXml="false" sortable="false" titleKey="meeting.uploaded_by"  style="width: 20%"/>
    <display:column property="lastUpdateDate" escapeXml="false" sortable="false" titleKey="note.last_update_date"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/lab/meeting/${meetingId}/document/${image.id}/download.html'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="meeting.view"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="file"/></fmt:message>')){deleteMeetingFile('${image.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="meeting.delete"/>'/></a>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="file"/>
    <display:setProperty name="paging.banner.items_name" value="files"/>

</display:table>