<%@ include file="/common/taglibs.jsp"%>
    <script type='text/javascript' src='<c:url value='/dwr/engine.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/dwr/util.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/scripts/order.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/dwr/interface/InventoryManager.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/scripts/storage.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/scripts/bime.js'/>'></script>
    
<a href="JavaScript:doMenu('sectionsArea');" id=xsectionsArea>[+]</a> <h3>Storage Sections</h3>
<div id=sectionsArea style="margin-left:1em">

<table cellpadding="0" class="table" cellspacing="0" width="60%">
  <thead>
    <tr>
      <th>Name</th>
      <th>Type</th>
      <th>Actions</th>
    </tr>
  </thead>
  <tbody id="sectionbody">
    <tr id="pattern" style="display:none;">
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
      <input type="button" value="Add New" onclick="addStorageSection()"/>
      <input type="button" value="Save Changes" onclick="saveStorageSection()"/>
      <input type="button" value="Clear" onclick="clearStorageSection()"/>
   </td>
  </tr>
</table>

</div>