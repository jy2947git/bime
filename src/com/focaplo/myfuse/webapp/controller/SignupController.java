package com.focaplo.myfuse.webapp.controller;


import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.exception.UserExistsException;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.RoleService;
import com.focaplo.myfuse.webapp.util.RequestUtil;

/**
 * Controller to signup new users.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class SignupController extends BaseFormController {
    private RoleService roleManager;

    public void setRoleManager(RoleService roleManager) {
        this.roleManager = roleManager;
    }

    public SignupController() {
        setCommandName("user");
        setCommandClass(User.class);
    }

    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response,
                                 Object command, BindException errors)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        User user = (User) command;
        Locale locale = request.getLocale();
        
        user.setEnabled(true);

        // Set the default user role on this new user
        user.addRole(roleManager.getRole(Constants.USER_ROLE));

        try {
            this.getUserManager().saveUser(user);
        } catch (AccessDeniedException ade) {
            // thrown by UserSecurityAdvice configured in aop:advisor userManagerSecurity
            log.warn(ade.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return null; 
        } catch (UserExistsException e) {
            errors.rejectValue("username", "errors.existing.user",
                    new Object[]{user.getUsername(), user.getEmail()}, "duplicate user");

            // redisplay the unencrypted passwords
            user.setPassword(user.getConfirmPassword());
            return showForm(request, response, errors);
        }

        saveMessage(request, getText("user.registered", user.getUsername(), locale));
        request.getSession().setAttribute(Constants.REGISTERED, Boolean.TRUE);

        // log user in automatically
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getConfirmPassword(), user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
        request.getSession().setAttribute("ACEGI_SECURITY_CONTEXT", SecurityContextHolder.getContext());
        // Send user an e-mail
        if (log.isDebugEnabled()) {
            log.debug("Sending user '" + user.getUsername() + "' an account information e-mail");
        }

        // Send an account information e-mail
        message.setSubject(getText("signup.email.subject", locale));

        try {
            sendUserMessage(user, getText("signup.email.message", locale), RequestUtil.getAppURL(request));
        } catch (MailException me) {
            saveError(request, me.getMostSpecificCause().getMessage());
        }
        
        return new ModelAndView(getSuccessView());
    }
}
