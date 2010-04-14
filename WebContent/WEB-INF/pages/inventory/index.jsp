<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="inventoryIndex.title"/></title>
    <meta name="heading" content="<fmt:message key='inventoryIndex.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
</head>

<p><fmt:message key="inventoryIndex.message"/></p>

<div class="separator"></div>

<ul class="glassList">
    <li>
        <a href="<c:url value='/refrigerator.html'/>"><fmt:message key="menu.inventory.refrigerator"/></a>
    </li>
    <li>
        <a href="<c:url value='/others.html'/>"><fmt:message key="menu.inventory.others"/></a>
    </li>
</ul>
