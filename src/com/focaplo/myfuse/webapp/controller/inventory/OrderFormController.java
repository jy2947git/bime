package com.focaplo.myfuse.webapp.controller.inventory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.model.ItemCategory;
import com.focaplo.myfuse.model.ManagedItem;
import com.focaplo.myfuse.model.ManagedOrder;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.util.RequestUtil;

@Controller

public class OrderFormController extends BimeFormController {

	
	@RequestMapping(value="/inventory/order/{orderId}/form.html", method=RequestMethod.POST)
	public String submitForm(@ModelAttribute("managedOrder")ManagedOrder order, BindingResult result, HttpServletRequest request, SessionStatus sessionStatus, Locale locale)throws Exception {

		if(request.getParameter("orderSubmit")!=null){
        	//find the super user
        	User loginUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	Long superUserId = loginUser.getSuperUserId();
        	User approvalUser = superUserId==null?loginUser:this.userManager.getUser(superUserId.toString());
        	if(approvalUser!=null){
        		String userName = request.getRemoteUser();
        		User submittedUser = this.userManager.getUserByUsername(userName);
        		this.orderManager.submitOrder(order.getId(), submittedUser.getId(),approvalUser.getId());
        		this.saveMessage(request, this.getText("order.submitted", locale));
        		try{
        			this.sendOrderForApprovalEmail(request, order, approvalUser);
        		}
        		catch (MailException me) {
                    saveError(request, me.getCause().getLocalizedMessage());
                }
        		return "redirect:/inventory/orders/list.html";
        	}else{
        		this.saveError(request, "errors.super.user.not.found.to.approve.order");
        		//TODO how to get back to original page, same as validation
        		return null;
        	}
        	 
        }else if(request.getParameter("orderApprove")!=null){
        	this.orderManager.approveOrder(order.getId(), ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
        	User approvalUser = this.userManager.getUser(order.getApprovedByPerson().toString());
        	User submittedUser = this.userManager.getUser(order.getSubmittedByPerson().toString());
        	this.saveMessage(request, this.getText("order.approved", locale));
        	try{
        		this.sendOrderApprovedEmail(request, order, submittedUser, approvalUser);
        	}
    		catch (MailException me) {
                saveError(request, me.getCause().getLocalizedMessage());
            }
        	return "redirect:/inventory/orders/list.html";
        }else if(request.getParameter("orderReject")!=null){
        	this.orderManager.rejectOrder(order.getId(), new Long(request.getParameter("id")));
        	User approvalUser = this.userManager.getUser(order.getApprovedByPerson().toString());
        	User submittedUser = this.userManager.getUser(order.getSubmittedByPerson().toString());
        	this.saveMessage(request, this.getText("order.rejected", locale));
        	try{
        		this.sendOrderRejectedEmail(request, order, submittedUser, approvalUser);
        	}catch (MailException me) {
                saveError(request, me.getCause().getLocalizedMessage());
            }
        	return "redirect:/inventory/orders/list.html";
        }else if(request.getParameter("orderInventorize")!=null){
        	List<ManagedItem> items = this.orderManager.inventorizeOrder(new Long(request.getParameter("id")));
        	for(ManagedItem item:items){
        		this.inventoryManager.addNotesToItem(item, this.getText("item.amount.added.by.peron", new Object[]{((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFullName(), item.getAmount()}, locale));
        	}
        	this.saveMessage(request, this.getText("order.item.inventorized", locale));
        	return "redirect:/inventory/orders/list.html";
        }else if (request.getParameter("method") != null && request.getParameter("method").equalsIgnoreCase("deleteOrderItem")) {
            this.orderManager.deleteOrderItem(new Long(request.getParameter("id")));
            saveMessage(request, getText("order.item.deleted", "order me me", locale));

            return "redirect:/inventory/orders/list.html";
        } else if (request.getParameter("orderCopy") != null) {
            this.orderManager.copyExistingOrder(order);
            saveMessage(request, getText("order.reordered", locale));

            return "redirect:/inventory/orders/list.html";
        } else {
        	Integer originalVersion = order.getVersion();
        	this.orderManager.saveOrder(order);
                 saveMessage(request, getText("order.saved",order.getId()+"", locale));
                 return "redirect:/inventory/order/" + order.getId()+"/form.html";

        }

	}
	
	@ModelAttribute("managedOrder")
	public ManagedOrder getOrder(@PathVariable(value="orderId") Long id){
		ManagedOrder order = null;
        
        if (id!=null&&id.intValue()>0) {
        	order = (ManagedOrder) this.orderManager.get(ManagedOrder.class, new Long(id));
        } else {
            order = new ManagedOrder();
            order.setStatus(Constants.order_draft);
//            user.addRole(new Role(Constants.USER_ROLE));
        }
       return order;
	}
	
	@RequestMapping(value="/inventory/order/{orderId}/form.html", method=RequestMethod.GET)
	public String showForm(){

            return "/inventory/orderForm";
	}
	

	private void sendOrderForApprovalEmail(HttpServletRequest request, ManagedOrder order,
			User approvalUser) {
		String userName = request.getRemoteUser();
		User submittedUser = this.userManager.getUserByUsername(userName);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(this.getText("email.order.submitted", request.getLocale()));
		message.setTo(approvalUser.getEmail());
		message.setCc(submittedUser.getEmail());
		Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("submittedUser", submittedUser);
        model.put("order", order);
        model.put("approvalUser", approvalUser);
        model.put("staticMessage", this.getText("email.order.submitted.body", request.getLocale()));
        model.put("applicationURL", RequestUtil.getAppURL(request));
		this.mailEngine.sendMessage(message, "orderSubmitted.vm", model);
	}
	
	private void sendOrderApprovedEmail(HttpServletRequest request, ManagedOrder order, User submittedUser,
			User approvalUser) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(this.getText("email.order.approved", request.getLocale()));
		message.setTo(approvalUser.getEmail());
		message.setCc(submittedUser.getEmail());
		Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("submittedUser", submittedUser);
        model.put("order", order);
        model.put("approvalUser", approvalUser);
        model.put("staticMessage", this.getText("email.order.approved.body", request.getLocale()));
        model.put("applicationURL", RequestUtil.getAppURL(request));
		this.mailEngine.sendMessage(message, "orderApproved.vm", model);
	}
	
	private void sendOrderRejectedEmail(HttpServletRequest request, ManagedOrder order, User submittedUser,
			User approvalUser) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(this.getText("email.order.rejected", request.getLocale()));
		message.setTo(approvalUser.getEmail());
		message.setCc(submittedUser.getEmail());
		Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("submittedUser", submittedUser);
        model.put("order", order);
        model.put("approvalUser", approvalUser);
        model.put("staticMessage", this.getText("email.order.rejected.body", request.getLocale()));
        model.put("applicationURL", RequestUtil.getAppURL(request));
		this.mailEngine.sendMessage(message, "orderRejected.vm", model);
	}
	
	
	@ModelAttribute("itemCategoryList")
	public List<ItemCategory> getAllCategories(){
		return this.inventoryManager.getAll(ItemCategory.class);
	}
}
