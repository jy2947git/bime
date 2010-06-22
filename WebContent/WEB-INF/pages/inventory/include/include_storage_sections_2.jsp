<%@ include file="/common/taglibs.jsp"%>

    <script type='text/javascript' src='<c:url value='/scripts/storage_sections_jquery.js'/>'></script>

 

<div id=sectionsArea style="margin-left:1em">

<table id="sectionTable" cellpadding="0" class="table" cellspacing="0" width="60%">
  <thead>
    <tr>
      <th width="200">Name</th>
      <th widht="200">Type</th>
      <th width="100">Actions</th>
    </tr>
  </thead>
  <tbody id="sectionbody">
       <tr id="pattern">
      <td>
        <span id="tableName">Name</span><br/>
      </td>
      <td><span id="tableType">Type</span></td>
      <td>
        <input id="edit" type="image" src="<c:url value='/images/pencil.png'/>" onclick="editClicked(this.id)"/>
        <input id="delete" type="image" src="<c:url value='/images/cross.png'/>" onclick="deleteClicked(this.id)"/>
      </td>
    </tr>
  </tbody>
</table>

<h3>Edit Section</h3>
<table class="plain">
	<input type="hidden" id="editSectionId" name="editSectionId" value="0"/>
  <tr>
    <td>Name:</td>
    <td><input id="editSectionName" type="text" size="30"/></td>
  </tr>
  <tr>
    <td>Type:</td>
    <td><input id="editSectionType" type="text" size="20"/></td>
  </tr>
  <tr>
    <td colspan="2" align="right">
      <span id="id"></span>
      <input type="button" id="addNewButton" value="Add New" onclick="addStorageSection()"/>
      <input type="button" id="saveChangeButton" value="Save Changes" onclick="saveStorageSection()" disabled="true"/>
      <input type="button" id="clearButton" value="Clear" onclick="clearStorageSection()" disabled="true"/>
   </td>
  </tr>
</table>

</div>