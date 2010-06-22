package com.focaplo.myfuse.webapp.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.exception.UnauthorizedAccessException;
import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.webapp.controller.BimeListController;

@Controller
public class UserListController extends BimeListController{


	@RequestMapping(value="/lab/users/list.html", method=RequestMethod.GET)
	public String displayList(Model model){
		this.checkAccess();
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "/admin/userList";
	}
	
	
	private void checkAccess() {
		if(!this.getLoginUser().getRoles().contains(roleManager.getRole(Constants.ADMIN_ROLE))){
			throw new UnauthorizedAccessException("User " + this.getLoginUser().getUsername() + " tried to access user list page!");

		}
		
	}


	@RequestMapping(value="/lab/user/{userId}/delete.html", method=RequestMethod.POST)
	public String delete(@PathVariable(value="userId") Long userId, Model model){
		this.deleteSelectedObject(userId);
		model.addAttribute( this.getListAttributeName(),super.getListOfSecuredObjects(null));
		return "redirect:/admin/userList";

	}



	@Override
	public Class<? extends BaseObject> getModelClass() {
		return User.class;
	}
}
