package com.bbz.test.registration;

import com.bbz.test.model.User;
import com.bbz.test.model.VerificationToken;
import com.bbz.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
//import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final Logger logger = LoggerFactory.getLogger(RegistrationListener.class);

    @Autowired
    UserService userService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        logger.debug(" onApplicationEvent(OnRegistrationCompleteEvent event)");
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        logger.debug("Trying to insert token " + token);
        VerificationToken token1 = userService.createVerificationToken(user, token);
        logger.debug("Saved token: " + token1);

        String recepientAdddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        String message = messageSource.getMessage("message.regSucc", null, event.getLocale());

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recepientAdddress);
        email.setSubject(subject);
        email.setText(message + " rn" + "http://localhost:8080" + confirmationUrl);
        mailSender.send(email);
    }
}
