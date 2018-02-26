package com.rawlead.github.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/users/signin")
    public String signin(){
        return "forms/signin";
    }

    @GetMapping(value = "/users/signup")
    public String signup() { return "forms/signup"; }

    @GetMapping(value = "/posts/{id}")
    public String singlePost(@PathVariable Long id) {
        return "post";
    }

    @GetMapping( value = "/profile")
    public String profile(@CookieValue(value = "access_token",required = false) String access_token) {
        if (access_token == null)
            return "redirect:/users/signin";
        return "profile";
    }

    @GetMapping(value = "/authors")
    public String authors() {
        return "authors";
    }
}
