package com.rawlead.github.controller;

import com.rawlead.github.entity.ResponseError;
import com.rawlead.github.entity.Role;
import com.rawlead.github.entity.User;
import com.rawlead.github.pojo.UserRegistrationForm;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class UserController {
    private UserService userService;
    private TokenStore tokenStore;

    @Autowired
    public UserController(UserService userService, TokenStore tokenStore) {
        this.userService = userService;
        this.tokenStore = tokenStore;
    }

    @PostMapping(value = "/users/signup", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody UserRegistrationForm userRegistrationForm) {
        Pattern loginPattern = Pattern.compile("[^a-zA-Z0-9]");
        Pattern emailPattern = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", Pattern.CASE_INSENSITIVE);

        if (userRegistrationForm.getEmail().trim().equals("") ||
                userRegistrationForm.getUsername().trim().equals("") ||
                userRegistrationForm.getPassword().trim().equals("") ||
                userRegistrationForm.getPasswordConfirm().trim().equals(""))
            return new ResponseEntity<>(ResponseError.EMPTY_FIELD, HttpStatus.CONFLICT);
        else if (userService.getUser(userRegistrationForm.getUsername()) != null)
            return new ResponseEntity<>(ResponseError.DUPLICATE_USER, HttpStatus.CONFLICT);
        else if (!userRegistrationForm.getPassword().equals(userRegistrationForm.getPasswordConfirm()))
            return new ResponseEntity<>(ResponseError.PASSWORD_MISMATCH, HttpStatus.CONFLICT);
        else if (loginPattern.matcher(userRegistrationForm.getUsername()).find())
            return new ResponseEntity<>(ResponseError.SPECIAL_CHARACTERS, HttpStatus.CONFLICT);
        else if (!emailPattern.matcher(userRegistrationForm.getEmail()).matches())
            return new ResponseEntity<>(ResponseError.INVALID_EMAIL, HttpStatus.CONFLICT);

        userService.save(new User(userRegistrationForm.getEmail(), userRegistrationForm.getUsername(), userRegistrationForm.getPassword(), Arrays.asList(new Role("USER"), new Role("PHOTOGRAPHER"))));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public List<User> users() {
        return userService.getAllUsers();
    }


    @GetMapping(value = "/users/signout")
    public void logout(@RequestParam(value = "access_token") String token) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
    }

    @GetMapping(value = "/getUsername")
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
