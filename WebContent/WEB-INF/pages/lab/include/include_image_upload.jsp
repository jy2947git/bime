<%@ include file="/common/taglibs.jsp"%>



<form:form commandName="fileUpload" method="post" action="upload.html" enctype="multipart/form-data"
    onsubmit="return validateFileUpload(this)" id="uploadForm">
    <form:hidden path="id"/>
    
<ul>
    <li class="info">
        <fmt:message key="upload.message"/>
    </li>

    <li>
        <appfuse:label key="uploadForm.file" styleClass="desc"/>
        <form:errors path="file" cssClass="fieldError"/>
        <input type="file" name="file" id="file" class="file medium"/>
    </li>
    <li class="buttonBar bottom">
        <input type="submit" name="upload" class="button" onclick="bCancel=false"
            value="<fmt:message key="button.upload"/>" />
        <input type="submit" name="cancel" class="button" onclick="bCancel=true"
            value="<fmt:message key="button.cancel"/>" />
    </li>
</ul>
</form:form>