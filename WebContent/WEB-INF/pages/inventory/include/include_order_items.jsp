<%@ include file="/common/taglibs.jsp"%>
    <script type='text/javascript' src='<c:url value='/dwr/interface/OrderManager.js'/>'></script>
  	<script type='text/javascript' src='<c:url value='/dwr/engine.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/dwr/util.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/scripts/order.js'/>'></script>
    <script type='text/javascript' src='<c:url value='/scripts/bime.js'/>'></script>
    
<a href="JavaScript:doMenu('sectionsArea');" id=xsectionsArea>[+]</a> <h1>Order Items</h1>
<div id=sectionsArea style="margin-left:1em">

<table cellpadding="0" class="table" cellspacing="0" width="80%">
  <thead>
    <tr>
      <th>Name</th>
      <th>Type</th>
      <th>Maker</th>
      <th>Amount</th>
      <th>Unit Price</th>
      <th>Total Cost</th>
      <th>Supplier</th>
      <th>Actions</th>
    </tr>
  </thead>
  <tbody id="sectionbody">
    <tr id="pattern" style="display:none;">
      <td>
        <span id="tableName">Name</span><br/>
      </td>
      <td><span id="tableType">Type</span></td>
      <td><span id="tableMaker">Maker</span></td>
      <td><span id="tableAmount">Amount</span></td>
      <td><span id="tableUnitPrice">Unit Price</span></td>
      <td><span id="tableTotalCost">Total Cost</span></td>
      <td><span id="tableSupplier">Supplier</span></td>
      
      <td>
       <input id="edit" type="image" src="<c:url value='/images/pencil.png'/>" onclick="editClicked(this.id)"/>
        <input id="delete" type="image" src="<c:url value='/images/cross.png'/>" onclick="deleteClicked(this.id)"/>
      </td>
    </tr>
  </tbody>
</table>

<h3>Edit Item</h3>
<form name="orderItemDetailForm">
<table class="plain">
	<input type="hidden" id="editItemId" name="editItemId" value="0"/>
  <tr>
    <td>Name:</td>
    <td>
    <select name="itemCategoryId">
    		<c:forEach items="${itemCategoryList}" var="itemCategory">
    			<option value="<c:out value="${itemCategory.id}"/>"><c:out value="${itemCategory.name}"/></option>
    		</c:forEach>
          <option value="">-----please select -----</option>
            
    </select>
   	</td>
  
   
  </tr>
  <tr>
    <td>Maker:</td>
    <td><input id="editItemMaker" type="text" size="20"/></td>
  
    <td>Amount:</td>
    <td><input id="editItemAmount" type="text" size="20" value="0"/></td>
  </tr>
  <tr>
    <td>Unit Price:</td>
    <td><input id="editItemUnitPrice" type="text" size="20" value="0.0"/></td>
  
    <td>Total Cost:</td>
    <td><input id="editItemTotalCost" type="text" size="20" readonly"></td>
  </tr>
  <tr>
    <td>Supplier:</td>
    <td><input id="editItemSupplier" type="text" size="20"/></td>
  </tr>
  
  <tr>
    <td colspan="2" align="right">
      <span id="id"></span>
      <input type="button" value="Add New" onclick="editItemTotalCost.value=editItemAmount.value*editItemUnitPrice.value;addOrderItem()"/>
      <input type="button" value="Save Changes" onclick="editItemTotalCost.value=editItemAmount.value*editItemUnitPrice.value;saveOrderItem()"/>
      <input type="button" value="Clear" onclick="clearOrderItem()"/>
   </td>
  </tr>
</table>
</form>
<v:javascript formName="orderItemDetail" staticJavascript="true"/>
</div>