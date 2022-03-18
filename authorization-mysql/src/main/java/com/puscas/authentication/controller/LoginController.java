package com.puscas.authentication.controller;

import com.fasterxml.uuid.impl.UUIDUtil;
import com.puscas.authentication.controller.model.UserDto;
import com.puscas.authentication.encryption.AesEncryptionUtil;
import com.puscas.authentication.event.OnRegistrationCompleteEvent;
import com.puscas.authentication.model.User;
import com.puscas.authentication.service.UserAlreadyExistException;
import com.puscas.authentication.service.interfacew.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Calendar;
import java.util.Locale;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    UserService userService;

    @Autowired
    private Environment environment;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "registration";
    }

    @PostMapping("/registration")
    public ModelAndView registerUserAccount(@ModelAttribute("user") @Valid UserDto userDto,
                                            HttpServletRequest request, Errors errors) {
        ModelAndView mav = new ModelAndView();

        if (errors.hasErrors()) {
            mav.setViewName("registration");
            mav.addObject("message", errors.getAllErrors());
        }

        try {
            User registered = userService.registerNewUserAccount(userDto);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, request.getLocale(),
                    request.getContextPath()));
        } catch (UserAlreadyExistException uaeEx) {
            mav.setViewName("registration");
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }
        mav.setViewName("login");
        return mav;
    }

    @GetMapping("/regitrationConfirm")
    public String confirmRegistration
            (WebRequest request, Model model, @RequestParam("token") String token) {

        Locale locale = request.getLocale();


        if (token == null) {
            //internationalization  String message = messages.getMessage("auth.message.invalidToken", null, locale);
            String message = "auth.message.invalidToken";
            model.addAttribute("message", message);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }
        String propertyToEncryptEmail = environment.getProperty("custom.email.validationKey");
        String saltToEncrypt = environment.getProperty("custom.email.salt");
        String decrypted = AesEncryptionUtil.decrypt(token, propertyToEncryptEmail, saltToEncrypt);
        Integer hoursToDecrypt = environment.getProperty("custom.email.validHours", Integer.class);
        String[] split = decrypted.split("###");

        UUID timeBasedUUid = UUID.fromString(split[1]);
        String email = split[0];
        Instant instantFromUUID = AesEncryptionUtil.getInstantFromUUID(timeBasedUUid);
        Calendar cal = Calendar.getInstance();
        if ((instantFromUUID.toEpochMilli() - (cal.getTime().getTime()- (hoursToDecrypt* 3600000))) <= 0) {
            //String messageValue = messages.getMessage("auth.message.expired", null, locale);
            String messageValue = "auth.message.expired";
            model.addAttribute("message", messageValue);
            return "redirect:/badUser.html?lang=" + locale.getLanguage();
        }

        userService.enableUser(email);
        return "redirect:/login.html?lang=" + request.getLocale().getLanguage();
    }
}
