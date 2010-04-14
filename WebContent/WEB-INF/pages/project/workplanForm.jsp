<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="note.title"/></title>
    <meta name="heading" content="<fmt:message key='workplan.heading'/>"/>
    <meta name="menu" content="ProjectMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
   
   	<script type="text/javascript">
    var GB_ROOT_DIR = "<c:url value='/scripts/greybox/'/>";
	</script>
	<script type="text/javascript" src="<c:url value='/scripts/greybox/AJS.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/greybox/AJS_fx.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/scripts/greybox/gb_scripts.js'/>"></script>
	<link href="<c:url value='/scripts/greybox/gb_styles.css'/>" rel="stylesheet" type="text/css" />
	
	        <link rel="stylesheet" type="text/css" media="all" href="<c:url value='/styles/${appConfig["csstheme"]}/theme.css'/>" />
        <link rel="stylesheet" type="text/css" media="print" href="<c:url value='/styles/${appConfig["csstheme"]}/print.css'/>" />

	    
</head>

<spring:bind path="workPlan.*">
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
<c:choose>
<c:when test="${monthly}"><c:set var="actionForm" value="workplanForm.html"/></c:when>
<c:otherwise><c:set var="actionForm" value="workplanWeeklyForm.html"/></c:otherwise>
</c:choose>
<form:form commandName="workPlan" method="post" action="${actionForm}" onsubmit="return onFormSubmit(this)" id="workPlanForm">
<form:hidden path="id"/>
<form:hidden path="version"/>
<input type="hidden" name="from" value="<c:out value="${param.from}"/>"/>
<input type="hidden" name="monthlyPlanId" value="<c:out value="${monthlyPlanId}"/>"/>

<ul>
    <li class="buttonBar right">
        <%-- So the buttons can be used at the bottom of the form --%>
        <c:set var="buttons">
            <input type="submit" class="button" name="save" onclick="bCancel=false" value="<fmt:message key="button.save"/>"/>

        <c:if test="${param.from == 'list' and param.method != 'Add'}">
            <input type="submit" class="button" name="delete" onclick="bCancel=true;return confirmDelete('work plan')"
                value="<fmt:message key="button.delete"/>"/>
        </c:if>

            <input type="submit" class="button" name="cancel" onclick="bCancel=true" value="<fmt:message key="button.cancel"/>"/>
        </c:set>
       
    </li>
    <li class="info">
        <c:choose>
            <c:when test="${param.from == 'list'}">
                <p><fmt:message key="workplan.update.message"/></p>
            </c:when>
            <c:otherwise>
                <p><fmt:message key="workplan.add.message"/></p>
            </c:otherwise>
        </c:choose>
    </li>

    <li>
		<div class="left">
			<appfuse:label styleClass="desc" key="workplan.type"/>
			<form:select path="planType">
            <form:options items="${planTypeLabelList}" itemValue="value" itemLabel="label"/>
            <option value="">------please select-----</option>
        	</form:select>
      	</div>
      	<div>
			<appfuse:label styleClass="desc" key="workplan.project"/>
			<form:select path="managedProject">
			
			<form:options items="${projectList}" itemLabel="name"/>
           <option value="">------please select-----</option>
        	</form:select>
      	</div>
    </li>
    <li>
		<div class="left">
			<appfuse:label styleClass="desc" key="workplan.identity"/>
			<c:choose>
            <c:when test="${monthly}"><c:set var="identityList" value="${monthLabelList}"/></c:when>
            <c:otherwise><c:set var="identityList" value="${weekOfMonthLabelList}"/></c:otherwise>
            </c:choose>
			<form:select path="planIdentity">
            <option value="">------please select-----</option>
            
            <form:options items="${identityList}" itemValue="value" itemLabel="label"/>
            
        	</form:select>
      	</div>
      	<div>
			<appfuse:label styleClass="desc" key="workplan.status"/>
			<form:select path="status">
            <option value="">------please select-----</option>
            
            <form:options items="${statusLabelList}" itemValue="value" itemLabel="label"/>
            
        	</form:select>
      	</div>
    </li>
        <li class="buttonBar bottom">
        <c:out value="${buttons}" escapeXml="false"/>
    </li>

</ul>

</form:form>

<c:if test="${workPlan.id != null}">



<div class="separator"></div>
<c:choose>
<c:when test="${monthly}">

<div align="right">
<a href="<c:url value='/project/workplanWeeklyForm.html?from=list&monthlyPlanId=${workPlan.id}'/>"><img src="<c:url value='/images/add.png'/>"/><fmt:message key="button.add_weekly_plan"/></a>
</div>
<display:table name="workPlan.weeklyPlans" cellspacing="0" cellpadding="0" requestURI="" 
    defaultsort="1" id="weeklyPlan" pagesize="25" class="table" export="false">

    <display:column escapeXml="true" sortable="true" titleKey="workplan.weekly.identity" style="width: 30%">
    	<fmt:message key="${weeklyPlan.planIdentity}"/>
    </display:column>
    <display:column escapeXml="true" sortable="true" titleKey="workplan.status" style="width: 30%">
    	<fmt:message key="${weeklyPlan.status}"/>
    </display:column>
    <display:column property="lastUpdateDate" escapeXml="false" sortable="true" titleKey="workplan.last_update_date"  format="{0,date,MM/dd/yyyy}" style="width: 20%"/> 
	<display:column style="width: 16%; padding-left: 15px" media="html">
        <a href="<c:url value='/project/workplanWeeklyForm.html?from=list&id=${weeklyPlan.id}'/>"><img src="<c:url value='/images/pencil.png'/>" alt="<fmt:message key="workPlan.edit"/>"/></a>&nbsp;
         <a href="<c:url value='/project/workplanForm.html?from=list&requestMethod=deleteWeeklyPlan&weeklyPlanId=${weeklyPlan.id}'/>"><img src="<c:url value='/images/cross.png'/>" alt="<fmt:message key="workPlan.delete"/>"/></a>
    </display:column>
    <display:setProperty name="paging.banner.item_name" value="weekly plans"/>
    <display:setProperty name="paging.banner.items_name" value="weekly plans"/>

</display:table>

</c:when>  
<c:otherwise>
<div align="right">
<a href="#" onclick="return GB_showCenter('plan item','<c:url value='/project/include/include_workPlanItemForm.html?from=list&planId=${workPlan.id}'/>',500,500,displayWorkPlanItems)"><fmt:message key="button.add_plan_item"/></a>
</div>
<div id="planItemsDiv">
</div>
<script type="text/javascript">
	function deleteWorkPlanItem(itemId){
		new Ajax.Updater('planItemsDiv','<c:url value='/project/include/include_workPlanItemListTable.html'/>',{method:'get', parameters:{from:'list',requestedMethod:'delete',selected:itemId,planId:<c:out value='${workPlan.id}'/>}});
	}
	function displayWorkPlanItems(){
		new Ajax.Updater('planItemsDiv','<c:url value='/project/include/include_workPlanItemListTable.html'/>',{method:'get', parameters:{from:'list',planId:<c:out value='${workPlan.id}'/>}});
	}
	displayWorkPlanItems();
</script>
</c:otherwise>
</c:choose>
</c:if>              
<script type="text/javascript">
    Form.focusFirstElement($('workPlanForm'));
    highlightFormElements();

  

<!-- This is here so we can exclude the selectAll call when roles is hidden -->
function onFormSubmit(theForm) {


    return validateWorkPlan(theForm);
}


</script>


        
        
<v:javascript formName="workPlanForm" staticJavascript="false"/>
<script type="text/javascript" src="<c:url value="/scripts/validator.jsp"/>"></script>

