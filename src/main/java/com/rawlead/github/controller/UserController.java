package com.rawlead.github.controller;

import com.rawlead.github.entity.ResponseMessage;
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

        userService.createNewUser(userRegistrationForm);

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

    @PutMapping(value = "/users/{userId}/updateAvatar")
    public ResponseEntity<?> updateAvatar(@RequestParam(value = "avatarImage") MultipartFile avatarImage, @PathVariable Long userId) {
        if (!userId.equals(getLoggedUser().getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        userService.updateUserAvatar(userId, avatarImage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/users/{userId}/deleteAvatar")
    public ResponseEntity<?> deleteAvatar(@PathVariable Long userId) {
        if (!userId.equals(getLoggedUser().getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (userService.deleteUserAvatar(userId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/users/{userId}/updateEmail")
    public ResponseEntity<?> updateEmail(@PathVariable Long userId, @RequestParam String newEmail, @RequestParam String newEmailConfirm) {
        if (!userId.equals(getLoggedUser().getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Pattern emailPattern = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", Pattern.CASE_INSENSITIVE);

        if (!emailPattern.matcher(newEmail).matches())
            return new ResponseEntity<>(ResponseMessage.INVALID_EMAIL, HttpStatus.CONFLICT);
        else if (!emailPattern.matcher(newEmailConfirm).matches())
            return new ResponseEntity<>(ResponseMessage.INVALID_EMAIL, HttpStatus.CONFLICT);
        else if (!newEmail.equals(newEmailConfirm))
            return new ResponseEntity<>(ResponseMessage.EMAIL_MISMATCH, HttpStatus.CONFLICT);
        userService.updateUserEmail(userId, newEmail, newEmailConfirm);
        return new ResponseEntity<>(ResponseMessage.EMAIL_UPDATED, HttpStatus.OK);
    }

    @PutMapping(value = "/users/{userId}/updatePassword")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestParam String oldPass, @RequestParam String newPass, @RequestParam String newPassConfirm) {
        if (!userId.equals(getLoggedUser().getId()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (oldPass.trim().equals("") ||
                newPass.trim().equals("") ||
                newPassConfirm.trim().equals(""))
            return new ResponseEntity<>(ResponseMessage.EMPTY_FIELD, HttpStatus.CONFLICT);
        else if (!newPass.equals(newPassConfirm))
            return new ResponseEntity<>(ResponseMessage.NEW_PASSWORDS_MISMATCH,HttpStatus.CONFLICT);

        if (!userService.updateUserPassword(userId, oldPass, newPass, newPassConfirm))
            return new ResponseEntity<>(ResponseMessage.CURRENT_PASSWORD_MISMATCH, HttpStatus.UNAUTHORIZED);

        return new ResponseEntity<>(ResponseMessage.PASSWORD_UPDATED,HttpStatus.OK);
    }
}
