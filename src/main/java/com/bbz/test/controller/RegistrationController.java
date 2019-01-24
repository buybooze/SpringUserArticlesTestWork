package com.bbz.test.controller;

import com.bbz.test.model.User;
import com.bbz.test.model.VerificationToken;
import com.bbz.test.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.Locale;

@Controller
public class RegistrationController {

    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmregistration(WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if(verificationToken == null) {
            String message = messageSource.getMessage("auth.message.invalidToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser?lang=" + locale.getLanguage();
        }

        User user = verificationToken.getUser();

        logger.debug("verificationToken.getUser(): " + verificationToken.getUser().toString());

        Calendar calendar = Calendar.getInstance();
        if(verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) {
            String message = messageSource.getMessage("auth.message.expiredToken", null, locale);
            model.addAttribute("message", message);
            return "redirect:/badUser?lang=" + locale.getLanguage();
        }

        user.setEnabled(true);
        userService.update(user);
        return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
    }
}
