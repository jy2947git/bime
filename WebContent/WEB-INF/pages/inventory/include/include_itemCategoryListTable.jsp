<%@ include file="/common/taglibs.jsp"%>
<display:table name="itemCategoryList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="itemCategory" pagesize="25" class="table" export="false">

    <display:column property="name" escapeXml="true" sortable="false" titleKey="itemCategory.name" style="width: 20%"/>
    <display:column property="type" escapeXml="true" sortable="false" titleKey="itemCategory.type" style="width: 20%"/>
        
   	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/inventory/itemCategory/${itemCategory.id}/form.html'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="itemCategory.edit"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="category"/></fmt:message>')){deleteItemCategory('${itemCategory.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="itemCategorydelete"/>'/></a>
    </display:column>

    <display:setProperty name="paging.banner.item_name" value="Experiment Material Category"/>
    <display:setProperty name="paging.banner.items_name" value="Experiment Material Categories"/>

</display:table>