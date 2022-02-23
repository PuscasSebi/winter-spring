package com.puscas.authentication.controller;

import com.puscas.authentication.controller.model.UserDto;
import com.puscas.authentication.model.User;
import com.puscas.authentication.service.UserAlreadyExistException;
import com.puscas.authentication.service.interfacew.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class LoginController {


    @Autowired
    UserService userService;

    @GetMapping("/login")
    public String login(){
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

        if(errors.hasErrors()){
            mav.setViewName("registration");
            mav.addObject("message", errors.getAllErrors());
        }

        try {
            User registered = userService.registerNewUserAccount(userDto);
        } catch (UserAlreadyExistException uaeEx) {
            mav.setViewName("registration");
            mav.addObject("message", "An account for that username/email already exists.");
            return mav;
        }
        mav.setViewName("login");
        return mav;
    }
}
