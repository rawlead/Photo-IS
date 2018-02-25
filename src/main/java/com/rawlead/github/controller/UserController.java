package com.rawlead.github.controller;

import com.rawlead.github.entity.ResponseMessage;
import com.rawlead.github.entity.Role;
import com.rawlead.github.entity.User;
import com.rawlead.github.pojo.UserRegistrationForm;
import com.rawlead.github.service.AmazonClient;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@RestController
public class UserController {
    private UserService userService;
    private TokenStore tokenStore;
    private AmazonClient amazonClient;

    @Autowired
    public UserController(UserService userService, TokenStore tokenStore, AmazonClient amazonClient) {
        this.userService = userService;
        this.tokenStore = tokenStore;
        this.amazonClient = amazonClient;
    }

    @PostMapping(value = "/users/signup", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody UserRegistrationForm userRegistrationForm) {
        Pattern loginPattern = Pattern.compile("[^a-zA-Z0-9]");
        Pattern emailPattern = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", Pattern.CASE_INSENSITIVE);

        if (userRegistrationForm.getEmail().trim().equals("") ||
                userRegistrationForm.getUsername().trim().equals("") ||
                userRegistrationForm.getPassword().trim().equals("") ||
                userRegistrationForm.getPasswordConfirm().trim().equals(""))
            return new ResponseEntity<>(ResponseMessage.EMPTY_FIELD, HttpStatus.CONFLICT);
        else if (userService.getUser(userRegistrationForm.getUsername()) != null)
            return new ResponseEntity<>(ResponseMessage.DUPLICATE_USER, HttpStatus.CONFLICT);
        else if (!userRegistrationForm.getPassword().equals(userRegistrationForm.getPasswordConfirm()))
            return new ResponseEntity<>(ResponseMessage.PASSWORD_MISMATCH, HttpStatus.CONFLICT);
        else if (loginPattern.matcher(userRegistrationForm.getUsername()).find())
            return new ResponseEntity<>(ResponseMessage.SPECIAL_CHARACTERS, HttpStatus.CONFLICT);
        else if (!emailPattern.matcher(userRegistrationForm.getEmail()).matches())
            return new ResponseEntity<>(ResponseMessage.INVALID_EMAIL, HttpStatus.CONFLICT);

        User user = new User();
        user.setEmail(userRegistrationForm.getEmail());
        user.setPassword(userService.getPasswordEncoder().encode(userRegistrationForm.getPassword()));
        user.setUsername(userRegistrationForm.getUsername());
        user.setRoles(Arrays.asList(new Role("USER"),new Role("PHOTOGRAPHER")));
        user.setAvatarUrl("");
        userService.save(user);

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

    @GetMapping(value = "/users/loggedUser")
    public User getLoggedUser() {
        return userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PutMapping(value = "/users/{id}/updateAvatar")
    public ResponseEntity<?> updateAvatar(@RequestParam(value = "avatarImage") MultipartFile avatarImage,@PathVariable Long id) {
        if (!id.equals(getLoggedUser().getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        userService.updateUserAvatar(id,avatarImage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{id}/deleteAvatar")
    public ResponseEntity<?> deleteAvatar(@PathVariable Long id) {
        if (!id.equals(getLoggedUser().getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if(userService.deleteUserAvatar(id))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
