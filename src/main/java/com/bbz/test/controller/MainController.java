package com.bbz.test.controller;

import com.bbz.test.dto.SearchFormDto;
import com.bbz.test.model.Article;
import com.bbz.test.dto.ArticleDto;
import com.bbz.test.model.User;
import com.bbz.test.dto.UserDto;
import com.bbz.test.registration.OnRegistrationCompleteEvent;
import com.bbz.test.service.ArticlesService;
import com.bbz.test.service.UserService;
import com.bbz.test.validation.exception.EmailExistsException;
import com.bbz.test.validation.exception.UserNameExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;


import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MainController {

    //Populate 'articles' table for testing (I failed to do it in db-test-data.sql)
    @PostConstruct
    public void populateDbArticles() throws Exception {
        List<Article> articleList = new ArrayList<>();
        articleList.add(new Article("admin","Lorem ipsum","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Mollis aliquam ut porttitor leo a diam sollicitudin. Commodo elit at imperdiet dui accumsan sit amet nulla. Consequat mauris nunc congue nisi. Enim tortor at auctor urna nunc. Nunc aliquet bibendum enim facilisis gravida neque convallis. In est ante in nibh mauris cursus mattis molestie a. Bibendum est ultricies integer quis auctor elit sed vulputate mi. Pretium fusce id velit ut tortor pretium. Etiam tempor orci eu lobortis elementum nibh tellus molestie. Nisl condimentum id venenatis a condimentum vitae sapien pellentesque habitant. Condimentum mattis pellentesque id nibh tortor id."));
        articleList.add(new Article("admin","Рагнар Рыжий","Жил да был Рагнар Рыжий — героем он слыл, как-то раз он в Вайтран ненадолго прибыл. Он куражился, пыжился, бряцал мечом, похваляясь, что враг ему всяк нипочём! Но вдруг Рагнар Рыжий как лютик поник, он услышал Матильды насмешливый крик…«Что блажишь ты, что врёшь, что ты мёд здесь наш пьёшь?! С нас довольно, готовься — сейчас ты умрёшь!» Лязг стали о сталь беспрестанно звенел, и Матильды воинственный дух пламенел! И унял с тех пор Рагнар хвастливую реееееечь… как слетела башка его рыжая с плеч!"));
        articleList.add(new Article("freddy","Я Фредди","Я динозавр, люблю фески, бабочки и бухать"));
        articlesService.saveBatch(articleList);
    }

    private final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    ArticlesService articlesService;

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ApplicationEventPublisher eventPublisher;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        return "redirect:/articles";
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    public String articles(Map<String, Object> model, Authentication authentication) {
        logger.debug("articles()");
        String currentLoggedInUser = "";
        if(authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            currentLoggedInUser = userDetails.getUsername();
        }
        model.put("searchFormDto", new SearchFormDto());
        model.put("authorList", userService.findAllUsers().stream().map(user -> user.getName()).collect(Collectors.toList()));
        model.put("currentLoggedInUser", currentLoggedInUser);
        model.put("articles", articlesService.findAll());
        return "index";
    }

    @RequestMapping(value = "/articles", method = RequestMethod.POST)
    public String articlesSearch(@ModelAttribute(value = "searchFormDto") SearchFormDto searchFormDto,
                           Map<String, Object> model, Authentication authentication) {
        logger.debug("articlesSearch()");
        String currentLoggedInUser = "";
        if(authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            currentLoggedInUser = userDetails.getUsername();
        }
        model.put("searchFormDto", new SearchFormDto());
        model.put("authorList", userService.findAllUsers().stream().map(user -> user.getName()).collect(Collectors.toList()));
        model.put("currentLoggedInUser", currentLoggedInUser);
        model.put("articles", articlesService.findByAuthorAndTitleContains(searchFormDto.getAuthor(), searchFormDto.getTitle()));
        return "index";
    }

    @RequestMapping(value = "/articles/show/{article_id}")
    public String showArticle(@PathVariable("article_id") int articleId, Map<String, Object> model, Authentication authentication) {
        logger.debug("showArticle()");
        String currentLoggedInUser = "";
        if(authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            currentLoggedInUser = userDetails.getUsername();
        }
        model.put("currentLoggedInUser", currentLoggedInUser);
        model.put("article", articlesService.findById(articleId));
        return "article";
    }

    @RequestMapping(value = "/articles/editor", method = RequestMethod.GET)
    public ModelAndView editor() {
        logger.debug("editor()");
        return new ModelAndView("editor", "articleDto", new ArticleDto());
    }

    @RequestMapping(value = "/articles/editor", method = RequestMethod.POST)
    public String saveArticle(@ModelAttribute("articleDto") @Valid ArticleDto articleDto, BindingResult result, Authentication authentication) {
        logger.debug("saveArticle()");
        if (result.hasErrors()) {
            return "editor";
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        articlesService.save(new Article(userDetails.getUsername(),articleDto.getTitle(),articleDto.getContent()));
       return "redirect:.";
    }

    @RequestMapping(value = "/articles/delete/{article_id}")
    public String deteleArticle(@PathVariable("article_id") int articleId, Authentication authentication) {
        logger.debug("deteleArticle()");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.debug("User has authorities: " + userDetails.getAuthorities());
        if(userDetails.getAuthorities().stream().anyMatch(authority -> ((GrantedAuthority) authority).equals(new SimpleGrantedAuthority("ROLE_ADMIN")))
                || userDetails.getUsername().equals(articlesService.findById(articleId).getAuthor())) {
            articlesService.delete(articleId);
        }
        return "redirect:../";
    }

    @RequestMapping(value = "/login")
    public String loginPage() {
        logger.debug("loginPage()");
        return "login";
    }

    @RequestMapping(value = "/login/registration", method = RequestMethod.GET)
    public ModelAndView register() {
        logger.debug("register() GET");
        return new ModelAndView("registration", "userDto", new UserDto());
    }

    @RequestMapping(value = "/login/registration", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute("userDto") @Valid UserDto userDto, BindingResult result, WebRequest request) {
        logger.debug("register() POST");
        logger.debug(userDto.toString());
        if (result.hasErrors()) {
            return new ModelAndView("registration", "userDto", userDto);
        }
        User registeredUser = createUserAccount(userDto, result);
        if (result.hasErrors()) {
            return new ModelAndView("registration", "userDto", userDto);
        }
        try {
            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, request.getLocale(), appUrl));
        } catch (Exception e) {
            return new ModelAndView("failRegistration", "user", registeredUser);
        }
        return new ModelAndView("successRegistration", "user", registeredUser);
    }

    private User createUserAccount(UserDto userDto, BindingResult result) {
        User user = null;
        try {
            user = userService.registerNewUserAccount((new User(userDto.getName(),userDto.getEmail().toLowerCase(),passwordEncoder.encode(userDto.getPassword()), Arrays.asList("ROLE_EDITOR"))));
        } catch (UserNameExistsException e) {
            result.addError(new FieldError("userDto","name","{email.alreadyExists}: "+e.getMessage()));
        } catch (EmailExistsException e) {
            result.addError(new FieldError("userDto", "email", "{nickname.alreadyExists}: "+e.getMessage()));
        }
        return user;
    }
}