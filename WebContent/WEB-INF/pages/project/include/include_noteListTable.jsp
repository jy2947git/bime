<%@ include file="/common/taglibs.jsp"%>


<display:table name="experimentNoteList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="note" pagesize="25" class="table" export="false">
    <display:column property="researcher.fullName" escapeXml="true" sortable="true" titleKey="note.researcherName" style="width: 20%"/>
    <display:column property="experimentProtocol.fullName" escapeXml="true" sortable="true" titleKey="note.protocol" style="width: 20%"/>
    
    <display:column property="lastUpdateDate" escapeXml="false" sortable="true" titleKey="note.last_update_date"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 

	<display:column style="width: 16%; padding-left: 15px" media="html">
	    <c:if test="${note.canEdit}">
    	 <a href="<c:url value='/project/${projectId}/note/${note.id}/form.html'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="note.edit"/>"/></a>
    	 </c:if>
    	&nbsp;
    	<c:if test="${note.canDelete}">
    	 <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="note"/></fmt:message>')){deleteNote('${note.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="note.delete"/>'/></a>
      	</c:if>
    	&nbsp;
   	   <c:if test="${note.isLocked}">
    		<img src="<c:url value='/images/lock.png'/>" alt="<fmt:message key="locked"/>"/>
    	</c:if>
        
    </display:column>
    
    <display:setProperty name="paging.banner.item_name" value="note"/>
    <display:setProperty name="paging.banner.items_name" value="notes"/>

</display:table>
