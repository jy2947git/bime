package com.focaplo.myfuse.webapp.controller.system;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import com.focaplo.myfuse.webapp.controller.admin.UserListController;
import com.focaplo.myfuse.webapp.listener.StartupListener;


/**
 * to reload configuration and spring....
 * Not used right now!
 */
@Controller
@Deprecated
public class ReloadController{
    private transient final Log log = LogFactory.getLog(UserListController.class);

    public ModelAndView handleRequest(HttpServletRequest request,
                                      HttpServletResponse response)
    throws Exception {
    	//FIXME refactor it later!
        if (log.isDebugEnabled()) {
            log.debug("Entering 'execute' method");
        }

        StartupListener.setupContext(request.getSession().getServletContext());

        String referer = request.getHeader("Referer");

        if (referer != null) {
            log.info("reload complete, reloading user back to: " + referer);
            List<String> messages = new ArrayList<String>();
            messages.add("Reloading options completed successfully.");
            request.getSession().setAttribute("messages", messages);
            response.sendRedirect(response.encodeRedirectURL(referer));
            return null;
        } else {
            response.setContentType("text/html");

            PrintWriter out = response.getWriter();

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Context Reloaded</title>");
            out.println("</head>");
            out.println("<body bgcolor=\"white\">");
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Context Reload Succeeded! Click OK to continue.');");
            out.println("history.back();");
            out.println("</script>");
            out.println("</body>");
            out.println("</html>");
        }

        return null;
    }
    
}
