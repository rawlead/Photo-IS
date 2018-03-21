package com.rawlead.github.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
// Przykład klasy-kontrolera
@Controller
public class HomeController {
    // Wszystkie HTTP zapytanie z metodą GET oraz śczieżką /photos
    // będą przekierowane do danej metody
    @GetMapping(value = "/photos")
    public String categories() {
        return "photos";
    }

    @GetMapping(value = "/")
    public String index() {
        return "redirect:/photos";
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
    public String singlePhoto() {
        return "photo";
    }


    @GetMapping(value = "/about")
    public String about() {
        return "about";
    }

}
