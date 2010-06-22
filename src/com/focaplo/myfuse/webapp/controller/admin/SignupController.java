package com.focaplo.myfuse.webapp.controller.admin;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.exception.UserExistsException;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.webapp.controller.BimeFormController;
import com.focaplo.myfuse.webapp.util.RequestUtil;



/**
 * not used
 *
 */
public class SignupController extends BimeFormController {

	@RequestMapping(value="/user/{userId}/form.html", method=RequestMethod.POST)
    public String submitForm(@ModelAttribute("user")User user, BindingResult result,  HttpServletRequest request, SessionStatus sessionStatus, Locale locale)
            throws Exception {
        user.setEnabled(true);

        // Set the default user role on this new user
        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        try {
            this.userManager.saveUser(user);
            //
            try {
            	this.sendEmail(request, user, locale);
            } catch (MailException me) {
                log.error("failed to send email", me);
             }
        } catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            log.warn(ade.getMessage());
            this.saveError(request, "user.access.denied");
            return null; 
        } catch (UserExistsException e) {
            this.saveError(request,
                   this.getText("error.user.already.exists", new Object[]{user.getUsername(), user.getEmail()}, locale));

            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            //FIXME back to the form page?
            return null;
        }

        saveMessage(request, getText("user.registered", user.getUsername(), locale));
        request.getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // log user in automatically
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getConfirmPassword(), user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
        request.getSession().setAttribute("ACEGI_SECURITY_CONTEXT", SecurityContextHolder.getContext());
       
        return "/mainMenu";
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
