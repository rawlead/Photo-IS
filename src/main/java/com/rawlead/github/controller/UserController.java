package com.rawlead.github.controller;

import com.rawlead.github.pojo.ResponseMessage;
import com.rawlead.github.entity.User;
import com.rawlead.github.pojo.UserRegistrationForm;
import com.rawlead.github.service.UserService;
import com.rawlead.github.service.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private TokenStore tokenStore;
    private UserService userService;
    private ValidatorService validatorService;

    @Autowired
    public UserController(TokenStore tokenStore, UserService userService, ValidatorService validatorService) {
        this.tokenStore = tokenStore;
        this.userService = userService;
        this.validatorService = validatorService;
    }

    private boolean isNotLoggedInUserMakesRequest(Long userId) {
        return !userId.equals(getLoggedUser().getId());
    }

    @PostMapping(value = "/signup", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody UserRegistrationForm userRegistrationForm) {
        return validatorService.signUp(userRegistrationForm);
    }

    @GetMapping
    public List<User> users() {
        return userService.getAllUsers();
    }

    @GetMapping(value ="/{username}")
    public ResponseEntity<?> userByUsername(@PathVariable String username) {
        User user = userService.getUser(username);
        if (user == null)
            return new ResponseEntity<>(ResponseMessage.USER_NOT_FOUND,HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @GetMapping(value = "/signout")
    public void logout(@RequestParam(value = "access_token") String token) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
    }

    @GetMapping(value = "/loggedUser")
    public User getLoggedUser() {
        return userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }


    @PutMapping(value = "/{userId}/avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam(value = "avatarImage") MultipartFile avatarImage, @PathVariable Long userId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        userService.updateUserAvatar(userId, avatarImage);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{userId}/avatar")
    public ResponseEntity<?> deleteAvatar(@PathVariable Long userId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (userService.deleteUserAvatar(userId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/{userId}/email")
    public ResponseEntity<?> updateEmail(@PathVariable Long userId, @RequestParam String newEmail, @RequestParam String newEmailConfirm) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return validatorService.updateEmail(userId, newEmail, newEmailConfirm);

    }

    @PutMapping(value = "/{userId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestParam String oldPass, @RequestParam String newPass, @RequestParam String newPassConfirm) {
        if(isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return validatorService.updatePassword(userId,oldPass,newPass,newPassConfirm);
    }

    @GetMapping(value = "/{userId}/favorite/users")
    public Set<User> getFavoriteUsers(@PathVariable Long userId) {
        return userService.getFavoriteUsers(userId);
    }

    @GetMapping(value = "/{userId}/favorite/users/{favoriteUserId}")
    public User getFavoriteUser(@PathVariable Long userId, @PathVariable Long favoriteUserId) {
         return userService.getFavoriteUser(userId,favoriteUserId);
    }

    @PostMapping(value = "/{userId}/favorite/users")
    public ResponseEntity<?> addFavoriteUser(@PathVariable Long userId, @RequestParam Long favoriteUserId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return validatorService.addFavoriteUser(userId, favoriteUserId);
    }

    @DeleteMapping(value = "/{userId}/favorite/users/{favoriteUserId}")
    public ResponseEntity<?> deleteFavoriteUser(@PathVariable Long userId, @PathVariable Long favoriteUserId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return validatorService.deleteFavoriteUser(userId,favoriteUserId);
    }
}






















