package com.focaplo.myfuse.webapp.controller.inventory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.focaplo.myfuse.model.OrderItem;
import com.focaplo.myfuse.model.Storage;
import com.focaplo.myfuse.model.StorageSection;
import com.focaplo.myfuse.webapp.controller.BimeFormController;

@Controller
public class OrderItemController extends BimeFormController{

	public class Item{
		//item category name and type, for display purpose only
		private String name;
		private  String type;
		private String maker;
		private  Long id;
		private Integer amount;
		private BigDecimal unitPrice;
		private BigDecimal totalCost;
		private String supplier;
		private Long itemCategoryId;
		
		public Long getItemCategoryId() {
			return itemCategoryId;
		}
		public void setItemCategoryId(Long itemCategoryId) {
			this.itemCategoryId = itemCategoryId;
		}
		public void setAmount(Integer amount) {
			this.amount = amount;
		}
		public String getMaker() {
			return maker;
		}
		public void setMaker(String maker) {
			this.maker = maker;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public int getAmount() {
			return amount;
		}
		public void setAmount(int amount) {
			this.amount = amount;
		}
		public BigDecimal getUnitPrice() {
			return unitPrice;
		}
		public void setUnitPrice(BigDecimal unitPrice) {
			this.unitPrice = unitPrice;
		}
		public BigDecimal getTotalCost() {
			return totalCost;
		}
		public void setTotalCost(BigDecimal totalCost) {
			this.totalCost = totalCost;
		}
		public String getSupplier() {
			return supplier;
		}
		public void setSupplier(String supplier) {
			this.supplier = supplier;
		}
		public Item(Long id, Long itemCategoryId, String name, String type, String maker,int amount,
				BigDecimal unitPrice, BigDecimal totalCost, String supplier) {
			super();
			this.name=name;
			this.type=type;
			this.maker= maker;
			this.id = id;
			this.amount = amount;
			this.unitPrice = unitPrice;
			this.totalCost = totalCost;
			this.supplier = supplier;
			this.itemCategoryId=itemCategoryId;
		}
		
	}
	@RequestMapping(value="/inventory/*/{orderId}/orderItems/list.*", method=RequestMethod.GET)
	@ResponseBody
	public String getListOfSectionsOfStorage(@PathVariable("orderId") Long orderId, Model model) throws JsonGenerationException, JsonMappingException, IOException{
		List<Item> sections = this.list(orderId);
		return toJson(sections);
	}
	
	
	
	private List<Item> list(Long orderId){
		return this.list( this.orderManager.getOrderItemsOfOrder(orderId));
	}
	
	private List<Item> list(List<OrderItem> orderItems){
		List<Item> ssss = new ArrayList<Item>();
		for(OrderItem ss:orderItems){
			Item s = new Item(ss.getId(),ss.getItemCategory().getId(),ss.getItemCategory().getName(),ss.getItemCategory().getType(), ss.getMaker(),ss.getAmount(),ss.getUnitPrice(),ss.getTotalCost(),ss.getSupplier());
			ssss.add(s);
		}
		return ssss;
	}
	@RequestMapping(value="/inventory/order/{orderId}/orderItem/{orderItemId}/delete.*", method=RequestMethod.POST)
	@ResponseBody
	public String deleteOrderItem(@PathVariable("orderId") Long orderId, @PathVariable("orderItemId") Long orderItemId, Model model) throws JsonGenerationException, JsonMappingException, IOException{
		this.orderManager.deleteOrderItem(orderItemId);
		List<Item> sections = this.list(orderId);
		return toJson(sections);
	
	}
	@RequestMapping(value="/inventory/order/{orderId}/orderItem/{orderItemId}/form.*", method=RequestMethod.POST)
	@ResponseBody
	public String saveOrderItem(@PathVariable("orderId") Long orderId, @PathVariable("orderItemId") Long orderItemId, @RequestParam(value="orderItemCategoryId", required=true) Long itemCategoryId, @RequestParam(value="orderItemMaker", required=false) String maker, @RequestParam("orderItemAmount") Integer amount, @RequestParam("orderItemUnitPrice") BigDecimal unitPrice, @RequestParam("orderItemTotalCost") BigDecimal totalCost, @RequestParam(value="orderItemSupplier", required=false) String supplier, Model model, Locale locale) throws JsonGenerationException, JsonMappingException, IOException{
		OrderItem orderItem = null;
		if(orderItemId!=null && orderItemId>0){
			orderItem = (OrderItem) this.orderManager.get(OrderItem.class, orderItemId);
		}else{
			orderItem = new OrderItem();
			orderItem.setCreatedDate(new Date());
			
		}
		orderItem.setItemCategoryId(itemCategoryId);
		orderItem.setMaker(maker);
		orderItem.setAmount(amount);
		orderItem.setUnitPrice(unitPrice);
		orderItem.setTotalCost(totalCost);
		orderItem.setSupplier(supplier);
		orderItem.setUpdatedByUser(this.getLoginUser().getUsername());
		this.orderManager.saveOrderItem(orderId, orderItem);
		List<Item> sections = this.list(orderId);
		return toJson(sections);
	}
}
