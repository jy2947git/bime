package com.focaplo.myfuse.service.impl;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;


/**
 * Class for sending e-mail messages based on Velocity templates
 * or with attachments.
 * 
 * @author Matt Raible
 */
@Service(value="mailEngine")
public class MailEngine {
    private final Log log = LogFactory.getLog(MailEngine.class);
    @Autowired
    private MailSender mailSender;
    @Autowired
    private VelocityEngine velocityEngine;
    private String defaultFrom;
    private boolean sendToQueue=false;



	public void setSendToQueue(boolean sendToQueue) {
		this.sendToQueue = sendToQueue;
	}

	public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setFrom(String from) {
        this.defaultFrom = from;
    }

    /**
     * Send a simple message based on a Velocity template.
     * @param msg the message to populate
     * @param templateName the Velocity template to use (relative to classpath)
     * @param model a map containing key/value pairs
     */
    public void sendMessage(SimpleMailMessage msg, String templateName, Map model) {
        String result = null;

        try {
            result =
                VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                                                            templateName, model);
        } catch (VelocityException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
       log.debug("message:" + result);
        msg.setText(result);
        try{
        	send(msg);
        }catch(MailSendException e){
        	log.error("email", e);
        }
    }

    /**
     * Send a simple message with pre-populated values.
     * @param msg the message to send
     * @throws org.springframework.mail.MailException when SMTP server is down
     */
    public void send(SimpleMailMessage msg) {
     

        mailSender.send(msg);
        	
        
    }

    /**
     * Convenience method for sending messages with attachments.
     * 
     * @param recipients array of e-mail addresses
     * @param sender e-mail address of sender
     * @param resource attachment from classpath
     * @param bodyText text in e-mail
     * @param subject subject of e-mail
     * @param attachmentName name for attachment
     * @throws MessagingException thrown when can't communicate with SMTP server
     */
    public void sendMessage(String[] recipients, String sender, 
                            ClassPathResource resource, String bodyText,
                            String subject, String attachmentName)
    throws MessagingException {
        MimeMessage message = ((JavaMailSenderImpl) mailSender).createMimeMessage();

        // use the true flag to indicate you need a multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipients);

        // use the default sending if no sender specified
        if (sender == null) {
            helper.setFrom(defaultFrom);
        } else {
           helper.setFrom(sender);
        }

        helper.setText(bodyText);
        helper.setSubject(subject);

        helper.addAttachment(attachmentName, resource);

        ((JavaMailSenderImpl) mailSender).send(message);
    }
}
