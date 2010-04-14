function init() {
  fillTable();
}

var itemCache = { };
var viewed = -1;

function fillTable() {
	  OrderManager.getOrderItemsOfOrder(orderId,function(items) {
	    // Delete all the rows except for the "pattern" row
	    dwr.util.removeAllRows("sectionbody", { filter:function(tr) {
	      return (tr.id != "pattern");
	    }});
	    // Create a new set cloned from the pattern row
	    var item, id;
	   
	    for (var i = 0; i < items.length; i++) {
	      item = items[i];
	      id = item.id;
	      dwr.util.cloneNode("pattern", { idSuffix:id });
	      dwr.util.setValue("tableName" + id, item.itemCategory==null?null:item.itemCategory.name);
	      dwr.util.setValue("tableType" + id, item.itemCategory==null?null:item.itemCategory.type);
	      dwr.util.setValue("tableMaker" + id, item.maker);
	      dwr.util.setValue("tableUnitPrice" + id, item.unitPrice);
	      dwr.util.setValue("tableTotalCost" + id, item.totalCost);
	      dwr.util.setValue("tableAmount" + id, item.amount);
	      dwr.util.setValue("tableSupplier" + id, item.supplier);
	      
	      $("pattern" + id).style.display = "table-row";
	     
	      itemCache[id] = item;
	    }
	  });
	}



function editClicked(eleid) {
  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
  var item = itemCache[eleid.substring(4)];
  viewed=item.id;
  //alert('item ' + item.id + ' ' + item.name + ' ' + item.type);
  dwr.util.setValues({editItemId:item.id,itemCategoryId:item.itemCategoryId, editItemMaker:item.maker,editItemUnitPrice:item.unitPrice,editItemAmount:item.amount,editItemTotalCost:item.totalCost,editItemSupplier:item.supplier});
}

function deleteClicked(eleid) {
  // we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
  var item = itemCache[eleid.substring(6)];
  if (confirm("Are you sure you want to delete " + item.name + "?")) {
    dwr.engine.beginBatch();
    OrderManager.deleteOrderItem(item.id);
    fillTable();
    dwr.engine.endBatch();
    
  }
}

function saveOrderItem() {

  dwr.engine.beginBatch();
  OrderManager.updateOrderItem(dwr.util.getValue('editItemId'),dwr.util.getValue('itemCategoryId'),dwr.util.getValue('editItemMaker'),dwr.util.getValue('editItemUnitPrice'),dwr.util.getValue('editItemAmount'),dwr.util.getValue('editItemTotalCost'),dwr.util.getValue('editItemSupplier'));
  
  fillTable();
  dwr.engine.endBatch();
  
  clearOrderItem();
}

function clearOrderItem() {
  viewed = -1;
  dwr.util.setValues({editItemId:0,itemCategoryId:null, editItemMaker:null,editItemUnitPrice:null,editItemAmount:null,editItemTotalCost:null,editItemSupplier:null});
}

function addOrderItem(){
	dwr.engine.beginBatch();
	OrderManager.saveOrderItem(orderId,{id:null,itemCategoryId:dwr.util.getValue('itemCategoryId'),maker:dwr.util.getValue('editItemMaker'),unitPrice:dwr.util.getValue('editItemUnitPrice'),amount:dwr.util.getValue('editItemAmount'),totalCost:dwr.util.getValue('editItemTotalCost'),supplier:dwr.util.getValue('editItemSupplier')});
	fillTable();
	
	dwr.engine.endBatch();
	
	clearOrderItem();
}
