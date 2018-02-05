package com.wiproevents.controllers;

import com.wiproevents.exceptions.ConfigurationException;
import com.wiproevents.exceptions.AttendeeException;
import com.wiproevents.utils.CustomMessageSource;
import com.wiproevents.utils.Helper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * The BaseEmail REST controller to provide email related methods.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEmailController {

    /**
     * The java mail sender.
     */
    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * The email template engine for body.
     */
    @Autowired
    private TemplateEngine bodyTemplateEngine;

    /**
     * The email template engine for subject.
     */
    @Autowired
    private TemplateEngine subjectTemplateEngine;

    /**
     * The default from email address.
     */
    @Value("${mail.from}")
    private String fromAddress;

    /**
     * Check if all required fields are initialized properly.
     *
     * @throws ConfigurationException if any required field is not initialized properly.
     */
    @PostConstruct
    protected void checkConfiguration() {
        Helper.checkConfigNotNull(javaMailSender, "javaMailSender");
        Helper.checkConfigNotNull(bodyTemplateEngine, "bodyTemplateEngine");
        Helper.checkConfigNotNull(subjectTemplateEngine, "subjectTemplateEngine");
        Helper.checkConfigState(Helper.isEmail(fromAddress), "fromAddress should be valid email address!");
    }

    /**
     * Send email with to email address, email name and model params.
     *
     * @param toEmail   the to email address.
     * @param emailName the email name.
     * @param context     the model params.
     * @throws AttendeeException throws if error to send email.
     */
    protected void sendEmail(String toEmail, String emailName, Context context) throws AttendeeException {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail);
            helper.setTo(toEmail);
            helper.setFrom(fromAddress);
            helper.setSubject(subjectTemplateEngine.process(emailName, context));
            helper.setText(bodyTemplateEngine.process(emailName, context), true);
            javaMailSender.send(mail);
        } catch (MessagingException | MailException e) {
            throw new AttendeeException(CustomMessageSource.getMessage("sendEmail.error"), e);
        }
    }
}
