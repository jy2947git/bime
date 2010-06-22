<%@ include file="/common/taglibs.jsp"%>

    <script type='text/javascript' src='<c:url value='/scripts/order_items_jquery.js'/>'></script>

  <h1>Order Items</h1>
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
  <tbody id="orderItembody">
    
  </tbody>
</table>

<h3>Edit Item</h3>
<form name="orderItemDetailForm">
<table class="plain">
	<input type="hidden" id="editOrderItemId" name="editOrderItemId" value="0"/>
  <tr>
    <td>Name:</td>
    <td>
    <select name="editOrderItemCategoryId" id="editOrderItemCategoryId">
    <option value="">-----please select -----</option>
    		<c:forEach items="${itemCategoryList}" var="itemCategory">
    			<option value="<c:out value="${itemCategory.id}"/>"><c:out value="${itemCategory.name}"/></option>
    		</c:forEach>
          
            
    </select>
   	</td>
  
   
  </tr>
  <tr>
    <td>Maker:</td>
    <td><input id="editOrderItemMaker" type="text" size="20"/></td>
  
    <td>Amount:</td>
    <td><input id="editOrderItemAmount" type="text" size="20" value="0"/></td>
  </tr>
  <tr>
    <td>Unit Price:</td>
    <td><input id="editOrderItemUnitPrice" type="text" size="20" value="0.0"/></td>
  
    <td>Total Cost:</td>
    <td><input id="editOrderItemTotalCost" type="text" size="20" readonly"></td>
  </tr>
  <tr>
    <td>Supplier:</td>
    <td><input id="editOrderItemSupplier" type="text" size="20"/></td>
  </tr>
  
  <tr>
    <td colspan="2" align="right">
      <span id="id"></span>
      <input type="button" id="addNewButton" value="Add New" onclick="editOrderItemTotalCost.value=editOrderItemAmount.value*editOrderItemUnitPrice.value;addEditOrderItem()"/>
      <input type="button" id="saveChangeButton" value="Save Changes" disabled="disabled" onclick="editOrderItemTotalCost.value=editOrderItemAmount.value*editOrderItemUnitPrice.value;saveEditOrderItem()"/>
      <input type="button" id="clearButton" value="Clear" onclick="clearEditOrderItem()"/>
   </td>
  </tr>
</table>
</form>
<v:javascript formName="orderItemDetail" staticJavascript="true"/>
</div>
<script type="text/javascript">
var orderId = <c:out value="${managedOrder.id}"/>;
getOrderItems(orderId);
</script>