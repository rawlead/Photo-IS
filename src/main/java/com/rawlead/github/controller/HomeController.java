package com.rawlead.github.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }



    @GetMapping(value = "/signin")
    public String signin() {
        return "forms/signin";
    }

    @GetMapping(value = "/signup")
    public String signup() {
        return "forms/signup";
    }

    @GetMapping(value = "/profile")
    public String profile(@CookieValue(value = "access_token", required = false) String access_token) {
        if (access_token == null)
            return "redirect:/signin";
        return "my_profile";
    }

    @GetMapping(value = "/authors")
    public String authors() {
        return "authors";
    }

    @GetMapping(value = "/profile/{username}")
    public String authorProfile() {
        return "author_profile";
    }

    @GetMapping(value = "/photo/{photoId}")
    public String singlePhoto() { return "photo"; }
}
