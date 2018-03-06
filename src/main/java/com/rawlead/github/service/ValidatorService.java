package com.rawlead.github.service;

import com.rawlead.github.pojo.UserRegistrationForm;
import org.springframework.http.ResponseEntity;

public interface ValidatorService {
    ResponseEntity<?> signUp(UserRegistrationForm userRegistrationForm);

    ResponseEntity<?> updateEmail(Long userId, String newEmail, String newEmailConfirm);

    ResponseEntity<?> updatePassword(Long userId, String oldPass, String newPass, String newPassConfirm);

    ResponseEntity<?> addFavoriteUser(Long userId, Long favoriteUserId);

    ResponseEntity<?> deleteFavoriteUser(Long userId, Long favoriteUserId);

    ResponseEntity<?> addFavoritePhoto(Long userId, Long favoritePhotoId);

    ResponseEntity<?> deleteFavoritePhoto(Long userId, Long favoritePhotoId);
}
