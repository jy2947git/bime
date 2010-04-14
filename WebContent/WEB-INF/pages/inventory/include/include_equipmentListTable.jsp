<%@ include file="/common/taglibs.jsp"%>
<display:table name="equipmentList" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="equipment" pagesize="25" class="table" export="false">
    <display:column property="name" escapeXml="true" sortable="false" titleKey="equipment.name" style="width: 20%"/>
    <display:column property="type" escapeXml="true" sortable="false" titleKey="equipment.type" style="width: 20%"/>
    
    
    <display:column property="location" escapeXml="true" sortable="false" titleKey="equipment.location" style="width: 15%"/>
    <display:column property="lastUserName" escapeXml="true" sortable="false" titleKey="equipment.last_user_name" style="width: 15%"/>
    
    <display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/inventory/equipmentForm.html?from=list&id=${equipment.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="equipment.edit"/>"/></a>&nbsp;
         <a href="javascript:if(confirm('<fmt:message key="confirm.delete"><fmt:param value="equipment"/></fmt:message>')){deleteEquipment('${equipment.id}')}"><img src="<c:url value='/images/cross.png'/>" alt='<fmt:message key="equipment.delete"/>'/></a>
    </display:column>

    <display:setProperty name="paging.banner.item_name" value="equipment"/>
    <display:setProperty name="paging.banner.items_name" value="equipments"/>

</display:table>