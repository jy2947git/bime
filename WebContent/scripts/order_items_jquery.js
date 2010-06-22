var itemCache = { };

function getOrderItems(orderId){
	
	  jQuery.ajax({
		type:"GET",
		url:"orderItems/list.json",
		data:"",
		contentType:"application/json; charset=urtf-8",
		dataType:"json",
		success:function(results){
		  //alert(results);
		  loadOrderItems(results);
		  },
		error:function(textStatus, errorThrown){ 
			  alert("Sorry failed to load the items " + textStatus + " " + errorThrown);
			  }
		
		
	});
}

function deleteOrderItem(orderItemId){
	  jQuery.ajax({
		type:"POST",
		url:"orderItem/"+orderItemId+"/delete.json",
		data:"{}",
		contentType:"application/json; charset=urtf-8",
		dataType:"json",
		success:function(data){loadOrderItems(data);},
		failure:function(){alert("Sorry failed to load the orderItems");}
		
		
	});
}

function saveOrderItem(orderItemId,itemCategoryId,orderItemMaker, orderItemAmount, orderItemUnitPrice, orderItemTotalCost, orderItemSupplier){

	//{'orderItemCategoryId':itemCategoryId,'orderItemMaker':orderItemMaker ,'orderItemAmount':orderItemAmount,'orderItemUnitPrice':orderItemUnitPrice,'orderItemTotalCost':orderItemTotalCost,'orderItemSupplier': orderItemSupplier},
	//"orderItemCategoryId="+itemCategoryId+ "&orderItemMaker=" + orderItemMaker + "&orderItemAmount=" + orderItemAmount + "&orderItemUnitPrice=" + orderItemUnitPrice + "&orderItemTotalCost=" + orderItemTotalCost + "&orderItemSupplier=" + orderItemSupplier
	jQuery.ajax({
		type:"POST",
		url:"orderItem/"+orderItemId+"/form.json"+"?"+"orderItemCategoryId="+itemCategoryId+ "&orderItemMaker=" + orderItemMaker + "&orderItemAmount=" + orderItemAmount + "&orderItemUnitPrice=" + orderItemUnitPrice + "&orderItemTotalCost=" + orderItemTotalCost + "&orderItemSupplier=" + orderItemSupplier,
		data:{},
		contentType:"application/json; charset=urtf-8",
		dataType:"json",
		success:function(data){loadOrderItems(data);},
		failure:function(){alert("Sorry failed to load the orderItems");}
		
		
	});
}

function loadOrderItems(data){
	//alert(data);
	itemCache={};
	jQuery('#orderItembody > tr').remove();
	for(var i =0; i<data.length;i++){
		var item = data[i];
		 itemCache[i] = item;
		//this is the Item class defined in the OrderItemController, not the OrderItem model class!
		//alert(item.name);
		var rowText = "<tr id=\"orderItem" + item.id+"\"><td>"+item.name+"</td><td>"+item.type+"</td><td>"+item.maker+"</td><td>"+item.amount+"</td><td>"+item.unitPrice+"</td><td>"+item.totalCost+"</td><td>"+item.supplier+"</td><td><input id=\"edit\" type=\"image\" src=\"/bime/images/pencil.png\" onclick=\"editOrderItem('edit"+i+"')\"/> &nbsp;&nbsp;<input type=\"image\" src=\"/bime/images/cross.png\" onclick=\"deleteEditOrderItem('delete"+ i+"')\"/></td></tr>";
		//alert(rowText);
		//jQuery(rowText).appendTo(tbody);
		jQuery('#orderItembody').append(rowText);
	}
}

//
// below functions are called from the page
//
function addEditOrderItem(){
	saveOrderItem(jQuery("#editOrderItemId").val(),jQuery("#editOrderItemCategoryId :selected").val(),jQuery("#editOrderItemMaker").val(),jQuery("#editOrderItemAmount").val(),jQuery("#editOrderItemUnitPrice").val(),jQuery("#editOrderItemTotalCost").val(),jQuery("#editOrderItemSupplier").val());
	clearEditOrderItem();
}

function saveEditOrderItem(){
	saveOrderItem(jQuery("#editOrderItemId").val(),jQuery("#editOrderItemCategoryId :selected").val(),jQuery("#editOrderItemMaker").val(),jQuery("#editOrderItemAmount").val(),jQuery("#editOrderItemUnitPrice").val(),jQuery("#editOrderItemTotalCost").val(),jQuery("#editOrderItemSupplier").val());
	clearEditOrderItem();
}

function clearEditOrderItem(){
	//clear the id, name and type
	jQuery("#editOrderItemId").val("0");
	jQuery("#editOrderItemCategoryId").val("");
	jQuery("#editOrderItemMaker").val("");
	jQuery("#editOrderItemAmount").val("0");
	jQuery("#editOrderItemUnitPrice").val("0.00");
	jQuery("#editOrderItemTotalCost").val("");
	jQuery("#editOrderItemSupplier").val("");
	jQuery("#saveChangeButton").attr("disabled", "true");
	jQuery("#addNewButton").removeAttr("disabled");
}

function deleteEditOrderItem(index){
	//find from cache by "delete12"
	 var item = itemCache[index.substring(6)];
	 deleteOrderItem(item.id);
}
function editOrderItem(index){
	//find from cache by "edit12"
	 var item = itemCache[index.substring(4)];
	//populate with value from row
	jQuery("#editOrderItemId").val(item.id);
	jQuery("#editOrderItemCategoryId").val(item.itemCategoryId);
	jQuery("#editOrderItemMaker").val(item.maker);
	jQuery("#editOrderItemAmount").val(item.amount);
	jQuery("#editOrderItemUnitPrice").val(item.unitPrice);
	jQuery("#editOrderItemTotalCost").val(item.totalCost);
	jQuery("#editOrderItemSupplier").val(item.supplier);
	//disable the add-new button
	jQuery("#addNewButton").attr("disabled", "true");
	jQuery("#saveChangeButton").removeAttr("disabled");
	jQuery("#clearButton").removeAttr("disabled");
}