<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.focaplo.myfuse.model.*" %>
<%@ page import="org.springframework.security.context.SecurityContextHolder"%>
<head>
    <title><fmt:message key="order.title"/></title>
    <meta name="heading" content="<fmt:message key='order.heading'/>"/>
    <meta name="menu" content="InventoryMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
    <script type='text/javascript' src='<c:url value='/dwr/engine.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/dwr/util.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/scripts/order.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/scripts/bime.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/scripts/order.js'/>'></script>
</head>

<spring:bind path="managedOrder.*">
    <c:if test="${not empty status.errorMessages}">
    <div class="error">
        <c:forEach var="error" items="${status.errorMessages}">
            <img src="<c:url value="/images/iconWarning.gif"/>"
                alt="<fmt:message key="icon.warning"/>" class="icon"/>
            <c:out value="${error}" escapeXml="false"/><br />
        </c:forEach>
    </div>
    </c:if>
</spring:bind>



<form:form commandName="managedOrder" method="post" action="orderForm.html" onsubmit="return onFormSubmit(this)" id="managedOrderForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>


<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        
        <c:set var="buttons">
        <c:if test="${managedOrder!=null && managedOrder.status==null || managedOrder.status<'5_order_approved'}">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>
		</c:if>
        <c:if test="${param.from == 'list' and param.method != 'Add' && managedOrder.status!=null && managedOrder.status<'2_order_submitted'}">
            <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('order')"
                value="<fmt:message key="button.delete"/>"/>
        </c:if>
		<c:if test="${managedOrder!=null && managedOrder.status!=null && managedOrder.status<'7_order_paid'}">
        <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
        </c:if>
        <c:if test="${managedOrder!=null && managedOrder.status!=null && (managedOrder.status=='1_order_draft' || managedOrder.status=='4_order_rejected')}">
        <input type="submit" class="button" name="orderSubmit" onclick="bCancel=false" value="<fmt:message key="button.order.submit"/>"/>
        </c:if>
       
        <c:if test="${managedOrder!=null && requestScope['principal_key'].isSuperUser && managedOrder.status!=null && managedOrder.status=='2_order_submitted'}">
        <input type="submit" class="button" name="orderApprove" onclick="bCancel=false" value="<fmt:message key="button.order.approve"/>"/>
        </c:if>
        
        <c:if test="${managedOrder!=null && requestScope['principal_key'].isSuperUser && managedOrder.status!=null && managedOrder.status=='2_order_submitted'}">
        <input type="submit" class="button" name="orderReject" onclick="bCancel=false" value="<fmt:message key="button.order.reject"/>"/>
        </c:if>
       
        <c:if test="${managedOrder!=null && managedOrder.status!=null && managedOrder.status=='5_order_approved'}">
        <input type="submit" class="button" name="orderInventorize" onclick="bCancel=false" value="<fmt:message key="button.order.inventory"/>"/>
        </c:if>
        
        <c:if test="${managedOrder!=null && managedOrder.status!=null && managedOrder.status>='5_order_approved'}">
        <input type="submit" class="button" name="orderCopy" onclick="bCancel=false" value="<fmt:message key="button.order.reorder"/>"/>
        </c:if>
        </c:set>
       
    </li>
    <li class="info">
        <c:choose>
            <c:when test="${param.from == 'list'}">
                <p><fmt:message key="order.update.message"/></p>
            </c:when>
            <c:otherwise>
                <p><fmt:message key="order.add.message"/></p>
            </c:otherwise>
        </c:choose>
    </li>
    
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="order.accountNumber"/>
            <form:errors path="accountNumber" cssClass="fieldError"/>
            <form:input path="accountNumber" id="accountNumber" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
		<div>
            <appfuse:label styleClass="desc" key="order.status"/>
           <input disabled="true" name="orderStatus" class="text medium" type="text"  maxlength="50" value="<fmt:message key="${managedOrder.status}"/>"/>
           
            
        </div>
    </li>
	<li>
        <div class="left">
            <appfuse:label styleClass="desc" key="order.fundName"/>
            <form:errors path="fundName" cssClass="fieldError"/>
            <form:input path="fundName" id="fundName" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
		<div>
            <appfuse:label styleClass="desc" key="order.totalPrice"/>
            <form:errors path="totalPrice" cssClass="fieldError"/>
            <form:input path="totalPrice" id="totalPrice" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
    </li>
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="order.salesFirstName"/>
            <form:errors path="salesFirstName" cssClass="fieldError"/>
            <form:input path="salesFirstName" id="SalesFirstName" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
		<div>
            <appfuse:label styleClass="desc" key="order.salesLastName"/>
            <form:errors path="salesLastName" cssClass="fieldError"/>
            <form:input path="salesLastName" id="totalPrice" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
    </li>
    <li>
        <div class="left">
            <appfuse:label styleClass="desc" key="order.salesPhone"/>
            <form:errors path="salesPhone" cssClass="fieldError"/>
            <form:input path="salesPhone" id="salesPhone" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
		<div>
            <appfuse:label styleClass="desc" key="order.salesEmail"/>
            <form:errors path="salesEmail" cssClass="fieldError"/>
            <form:input path="salesEmail" id="totalPrice" cssClass="text medium" cssErrorClass="text medium error" maxlength="50"/>
        </div>
    </li>
    <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>
</ul>
</form:form>

<c:choose>
<c:when test="${managedOrder.id!=null}">
<jsp:include page="include_order_items.jsp"></jsp:include>
<script type="text/javascript">
	var orderId = <c:out value="${managedOrder.id}"/>;
	init();
</script>
</c:when>
</c:choose>
<script type="text/javascript">
    Form.focusFirstElement($('managedOrderForm'));
    highlightFormElements();

  

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {
    return validateManagedOrder(theForm);
}
</script>

<v:javascript formName="managedOrder" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

