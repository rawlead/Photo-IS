package com.rawlead.github.service.impl;

import com.rawlead.github.pojo.ResponseMessage;
import com.rawlead.github.pojo.UserRegistrationForm;
import com.rawlead.github.service.PhotoService;
import com.rawlead.github.service.UserService;
import com.rawlead.github.service.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ValidatorServiceImpl implements ValidatorService {
    private UserService userService;
    private PhotoService photoService;

    @Autowired
    public ValidatorServiceImpl(UserService userService, PhotoService photoService) {
        this.userService = userService;
        this.photoService = photoService;
    }

    @Override
    public ResponseEntity<?> signUp(UserRegistrationForm userRegistrationForm) {
        Pattern loginPattern = Pattern.compile("[^a-zA-Z0-9]");
        Pattern emailPattern = Pattern.compile("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$", Pattern.CASE_INSENSITIVE);
        if (userRegistrationForm.getFirstName().trim().equals("") ||
                userRegistrationForm.getEmail().trim().equals("") ||
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
        userService.save(userRegistrationForm);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> updateEmail(Long userId, String newEmail, String newEmailConfirm) {
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

    @Override
    public ResponseEntity<?> updatePassword(Long userId, String oldPass, String newPass, String newPassConfirm) {
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

    @Override
    public ResponseEntity<?> addFavoriteUser(Long userId, Long favoriteUserId) {
        if(userId.equals(favoriteUserId))
            return new ResponseEntity<>(ResponseMessage.FAVORITE_MYSELF, HttpStatus.CONFLICT);
        else if (userService.addFavoriteUser(userId,favoriteUserId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(ResponseMessage.USER_ALREADY_FAVORITE_OR_DOESNT_EXIST,HttpStatus.CONFLICT);
    }

    @Override
    public ResponseEntity<?> deleteFavoriteUser(Long userId, Long favoriteUserId) {
        if(userId.equals(favoriteUserId))
            return new ResponseEntity<>(ResponseMessage.FAVORITE_MYSELF, HttpStatus.CONFLICT);
        else if (userService.deleteFavoriteUser(userId,favoriteUserId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(ResponseMessage.USER_NOT_FOUND,HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> addFavoritePhoto(Long userId, Long favoritePhotoId) {
        if (photoService.addFavoritePhoto(userId,favoritePhotoId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(ResponseMessage.PHOTO_ALREADY_FAVORITE_OR_DOESNT_EXIST, HttpStatus.CONFLICT);
    }

    @Override
    public ResponseEntity<?> deleteFavoritePhoto(Long userId, Long favoritePhotoId) {
         if (photoService.deleteFavoritePhoto(userId,favoritePhotoId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
}
