package com.rawlead.github.controller;

import com.rawlead.github.pojo.ResponseMessage;
import com.rawlead.github.entity.User;
import com.rawlead.github.pojo.UserRegistrationForm;
import com.rawlead.github.service.PhotoService;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private TokenStore tokenStore;
    private UserService userService;

    Pattern LOGIN_PATTERN = Pattern.compile("[^a-zA-Z0-9]");
    Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", Pattern.CASE_INSENSITIVE);

    @Autowired
    public UserController(TokenStore tokenStore, UserService userService) {
        this.tokenStore = tokenStore;
        this.userService = userService;
    }

    private boolean isNotLoggedInUserMakesRequest(Long userId) {
        return !userId.equals(getLoggedUser().getId());
    }


    @PostMapping(value = "/signup", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody UserRegistrationForm userRegistrationForm) {
        if (userRegistrationForm.getFirstName().trim().isEmpty() ||
                userRegistrationForm.getEmail().trim().isEmpty() ||
                userRegistrationForm.getUsername().trim().isEmpty() ||
                userRegistrationForm.getPassword().trim().isEmpty() ||
                userRegistrationForm.getPasswordConfirm().trim().isEmpty())
            return new ResponseEntity<>(ResponseMessage.EMPTY_FIELD, HttpStatus.CONFLICT);
        else if (userService.getUser(userRegistrationForm.getUsername()) != null)
            return new ResponseEntity<>(ResponseMessage.DUPLICATE_USER, HttpStatus.CONFLICT);
        else if (userRegistrationForm.getUsername().length() <= 1)
            return new ResponseEntity<>(ResponseMessage.USERNAME_SIZE, HttpStatus.CONFLICT);
        else if (userRegistrationForm.getPassword().length() < 6)
            return new ResponseEntity<>(ResponseMessage.PASSWORD_SIZE, HttpStatus.CONFLICT);
        else if (!userRegistrationForm.getPassword().equals(userRegistrationForm.getPasswordConfirm()))
            return new ResponseEntity<>(ResponseMessage.PASSWORD_MISMATCH, HttpStatus.CONFLICT);
        else if (LOGIN_PATTERN.matcher(userRegistrationForm.getUsername()).find())
            return new ResponseEntity<>(ResponseMessage.SPECIAL_CHARACTERS, HttpStatus.CONFLICT);
        else if (!EMAIL_PATTERN.matcher(userRegistrationForm.getEmail()).matches())
            return new ResponseEntity<>(ResponseMessage.INVALID_EMAIL, HttpStatus.CONFLICT);
        userService.save(userRegistrationForm);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public List<User> users() {
        return userService.listAllUsers();
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<?> userByUsername(@PathVariable String username) {
        User user = userService.getUser(username);
        if (user == null)
            return new ResponseEntity<>(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
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
        if (userService.updateUserAvatar(userId, avatarImage) != null)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
        if (!EMAIL_PATTERN.matcher(newEmail).matches())
            return new ResponseEntity<>(ResponseMessage.INVALID_EMAIL, HttpStatus.CONFLICT);
        else if (!EMAIL_PATTERN.matcher(newEmailConfirm).matches())
            return new ResponseEntity<>(ResponseMessage.INVALID_EMAIL, HttpStatus.CONFLICT);
        else if (!newEmail.equals(newEmailConfirm))
            return new ResponseEntity<>(ResponseMessage.EMAIL_MISMATCH, HttpStatus.CONFLICT);
        userService.updateUserEmail(userId, newEmail, newEmailConfirm);
        return new ResponseEntity<>(ResponseMessage.EMAIL_UPDATED, HttpStatus.OK);
    }

    @PutMapping(value = "/{userId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable Long userId, @RequestParam String oldPass, @RequestParam String newPass, @RequestParam String newPassConfirm) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (oldPass.trim().equals("") ||
                newPass.trim().equals("") ||
                newPassConfirm.trim().equals(""))
            return new ResponseEntity<>(ResponseMessage.EMPTY_FIELD, HttpStatus.CONFLICT);
        else if (!newPass.equals(newPassConfirm))
            return new ResponseEntity<>(ResponseMessage.NEW_PASSWORDS_MISMATCH, HttpStatus.CONFLICT);
        if (!userService.updateUserPassword(userId, oldPass, newPass, newPassConfirm))
            return new ResponseEntity<>(ResponseMessage.CURRENT_PASSWORD_MISMATCH, HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(ResponseMessage.PASSWORD_UPDATED, HttpStatus.OK);
    }


    @GetMapping(value = "/{userId}/favorite/users")
    public List<User> getFavoriteUsers(@PathVariable Long userId) {
        return userService.listFavoriteUsers(userId);
    }


    @GetMapping(value = "/{userId}/favorite/users/{favoriteUserId}")
    public User getFavoriteUser(@PathVariable Long userId, @PathVariable Long favoriteUserId) {
        return userService.getFavoriteUser(userId, favoriteUserId);
    }


    @PostMapping(value = "/{userId}/favorite/users")
    public ResponseEntity<?> addFavoriteUser(@PathVariable Long userId, @RequestParam Long favoriteUserId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (userId.equals(favoriteUserId))
            return new ResponseEntity<>(ResponseMessage.FAVORITE_MYSELF, HttpStatus.CONFLICT);
        else if (userService.addFavoriteUser(userId, favoriteUserId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(ResponseMessage.USER_ALREADY_FAVORITE_OR_DOESNT_EXIST, HttpStatus.CONFLICT);
    }


    @DeleteMapping(value = "/{userId}/favorite/users/{favoriteUserId}")
    public ResponseEntity<?> deleteFavoriteUser(@PathVariable Long userId, @PathVariable Long favoriteUserId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (userId.equals(favoriteUserId))
            return new ResponseEntity<>(ResponseMessage.FAVORITE_MYSELF, HttpStatus.CONFLICT);
        else if (userService.deleteFavoriteUser(userId, favoriteUserId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}






















