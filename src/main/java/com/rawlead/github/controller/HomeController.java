package com.rawlead.github.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping(value = "/profile")
    public String profile() {
        return "profile";
    }
}
