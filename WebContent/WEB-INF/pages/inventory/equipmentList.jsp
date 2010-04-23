<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="equipmentList.title"/></title>
    <meta name="heading" content="<fmt:message key='equipmentList.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>

<c:set var="buttons">
    <input type="button" style="margin-right: 5px"
        onclick="location.href='<c:url value="/inventory/equipmentForm.html?method=Add&from=list"/>'"
        value="<fmt:message key="button.add"/>"/>

    <input type="submit" style="margin-right: 5px"
        
        value="<fmt:message key="button.delete"/>"/>
</c:set>

<div align="right">
<a href="<c:url value='/inventory/equipmentForm.html?method=Add&from=list'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="equipment.add"/></a>
</div>
<div id="myListDiv">
<jsp:include page="include/include_equipmentListTable.jsp"></jsp:include>
</div>
<script type="text/javascript">
	function deleteEquipment(itemId){
		new Ajax.Updater('myListDiv','<c:url value='/inventory/include/include_equipmentListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId}});
	}
</script>
