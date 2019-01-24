package com.bbz.test.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    @PostConstruct
    public void printWTF() {
        logger.debug("WTF CustomAuthenticationFailureHandler");
    }

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
        throws IOException, ServletException {
        logger.debug("onAuthenticationFailure()");


            setDefaultFailureUrl("/login?error=true");

            super.onAuthenticationFailure(request,response,exception);

            Locale locale = localeResolver.resolveLocale(request);

            String errorMessage = messageSource.getMessage("message.badCredentials", null, locale);


            logger.debug("WTF WENT WRONG: " + exception.getMessage());

            if(exception.getMessage().equalsIgnoreCase("User is disabled")) {
                logger.debug("DISABLED MOTHERFUCKER");
                errorMessage = messageSource.getMessage("auth.message.disabled", null, locale);
            } else if(exception.getMessage().equalsIgnoreCase("User account has expired")) {
                errorMessage = messageSource.getMessage("auth.message.expired", null, locale);
            }


            logger.debug("trying to pass message: " + errorMessage);

            request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);


        for (Enumeration<String> e = request.getSession().getAttributeNames(); e.hasMoreElements();)
            logger.debug(e.nextElement());


    }
}
