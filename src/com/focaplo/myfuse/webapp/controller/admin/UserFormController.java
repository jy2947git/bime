package com.focaplo.myfuse.webapp.controller.admin;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.exception.UnauthorizedAccessException;
import com.focaplo.myfuse.exception.UserExistsException;
import com.focaplo.myfuse.model.BaseObject;
import com.focaplo.myfuse.model.Role;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.CacheService;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.util.RequestUtil;


@Controller
public class UserFormController extends BimeFormController {

    
	@ModelAttribute("user")
	public User getUser(@PathVariable("userId") Long userId){
        // if user logged in with remember me, display a warning that they can't change passwords
        log.debug("checking for remember me login...");

        AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
        SecurityContext ctx = SecurityContextHolder.getContext();

        if (ctx.getAuthentication() != null) {
            Authentication auth = ctx.getAuthentication();

            if (resolver.isRememberMe(auth)) {
            
            	log.warn("user " + userId + "  log in with remember-me");
//                request.getSession().setAttribute("cookieLogin", "true");
//
//                // add warning message
//                saveMessage(request, getText("userProfile.cookieLogin", request.getLocale()));
            }
        }
        
		if(userId!=null&&userId.longValue()>0){
			return this.userManager.getUser(userId.toString());
		}else{
			User user = new User();
			user.addRole(new Role(Constants.USER_ROLE));
			return user;
		}
	}
	
	@RequestMapping(value="/lab/user/{userId}/form.html", method=RequestMethod.POST)
	public String submitForm(@PathVariable("userId") Long userId, @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request, SessionStatus sessionStatus, Locale locale) throws Exception {
		checkAccess(userId, null);
            // only attempt to change roles if user is admin for other users,
            // formBackingObject() method will handle populating
            if (request.isUserInRole(Constants.ADMIN_ROLE)) {
                String[] userRoles = request.getParameterValues("userRoles");

                if (userRoles != null) {
                    user.getRoles().clear();
                    for (String roleName : userRoles) {
                        user.addRole(roleManager.getRole(roleName));
                    }
                }
            }

            Integer originalVersion = user.getVersion();
            
            try {
            	//FIXME - regular user can only change the name, password etc etc
            	//ADMIN can change super user and roles etc
            	//need to use different methods based on the login user role
               userManager.saveUser(user);
               this.expireCachedObjects(User.class, user.getId());
               if(user.isSuperUser()&&this.cacheManager!=null&&this.cacheManager.isOn()){
            	   this.cacheManager.delete("super-users"+"-list");
               }
               saveMessage(request, getText("user.updated.byAdmin", user.getFullName(), locale));
               //if the first time then send email
               if(originalVersion==null){
            	   this.sendEmail(request, user, locale);
               }
            } catch (AccessDeniedException ade) {
            	log.error("access denied", ade);
            	throw new UnauthorizedAccessException("User " + this.getLoginUser().getUsername() + " tried to access user id " + userId);

            	//FIXME refactor
            } catch (UserExistsException e) {
            	this.saveError(request, "error.user.already.exists");
            	//FIXME refactor
            }
            if(this.getLoginUser().getRoles().contains(roleManager.getRole(Constants.ADMIN_ROLE))){
            	 return "redirect:/lab/users/list.html";
            }else{
            	return "redirect:/mainMenu.html";
            }
           
        }
	
	@RequestMapping(value="/lab/user/{userId}/form.html", method=RequestMethod.GET)
	public String showForm(@PathVariable("userId") Long userId, Model model){
		checkAccess(userId, model);
		model.addAttribute("superUserList", this.getSuperUsers());

        return "/userForm";
		
	}

	protected List getSuperUsers(){
		List objects = null;
		//use cache
		if(this.cacheManager!=null&&this.cacheManager.isOn()){
			objects  = (List<? extends BaseObject>) this.cacheManager.get("super-users"+"-list");
		}
		if(objects==null){
			objects = this.userManager.getUsersWithRole(Constants.SUPER_USER_ROLE);
			if(this.cacheManager!=null&&this.cacheManager.isOn()){
				this.cacheManager.set("super-users"+"-list", CacheService.default_ttl_in_seconds, objects);
			}
		}
		return objects;
	}
	private void checkAccess(@PathVariable("userId") Long requestedUserId, Model model) {
		if(!this.getLoginUser().getRoles().contains(roleManager.getRole(Constants.ADMIN_ROLE))){
			if(model!=null){
				model.addAttribute("admin", Boolean.FALSE);
			}
			if(this.getLoginUser().getId().longValue()!=requestedUserId.longValue()){
				throw new UnauthorizedAccessException("User " + this.getLoginUser().getUsername() + " tried to access user id " + requestedUserId);
			}
		}else{
			if(model!=null){
				model.addAttribute("admin", Boolean.TRUE);
			}
		}
		
	}
	
	public void sendEmail(HttpServletRequest request, User user, Locale locale){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject(this.getText("signup.email.subject",locale));
		message.setTo(user.getEmail());
//		message.setCc(user.getSuperUserId().getEmail());
		Map<String, Serializable> model = new HashMap<String, Serializable>();
        model.put("user", user);
        model.put("message", this.getText("signup.email.message", request.getLocale()));
        model.put("applicationURL", RequestUtil.getAppURL(request));
		this.mailEngine.sendMessage(message, "accountCreated.vm", model);
	}
}
