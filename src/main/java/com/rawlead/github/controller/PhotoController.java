package com.rawlead.github.controller;

import com.rawlead.github.ResponseMessage;
import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.User;
import com.rawlead.github.service.PhotoService;
import com.rawlead.github.service.UserService;
import com.rawlead.github.service.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
public class PhotoController {
    private PhotoService photoService;
    private UserService userService;
    private Validator validator;

    @Autowired
    public PhotoController(PhotoService photoService, UserService userService, Validator validator) {
        this.photoService = photoService;
        this.userService = userService;
        this.validator = validator;
    }

    private boolean isNotLoggedInUserMakesRequest(Long userId) {
        return !userId.equals(getLoggedUser().getId());
    }

    private User getLoggedUser() {
        return userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping(value = "/api/photos")
    public List<Photo> posts() {
        return photoService.getAllPhotos();
    }

    @GetMapping(value = "/api/users/{userId}/photos")
    public List<Photo> getPhotosByUser(@PathVariable Long userId) {
        return photoService.findByUser(userId);
    }

    @PostMapping(value = "/api/users/{userId}/photos")
    public ResponseEntity<?> addPhoto(@RequestPart MultipartFile photo,
                                       @RequestPart(required = false) String title,
                                       @RequestPart(required = false) String description,
                                       @RequestPart String category,
                                       @PathVariable Long userId) {
        this.photoService.addPhoto(userId,photo,title,description,category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/users/{userId}/photos/{photoId}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long userId, @PathVariable Long photoId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        this.photoService.deletePhoto(photoId);
        return new ResponseEntity<>(ResponseMessage.PHOTO_DELETED,HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/photos/{id}")
    public boolean delete(@PathVariable Long id) {
        return photoService.deletePhoto(id);
    }

    @GetMapping(value = "/api/users/{userId}/favorite/photos")
    public Set<Photo> getFavoritePhotos(@PathVariable Long userId) {
        return photoService.getFavoritePhotos(userId);
    }

//    @GetMapping(value = "/api/users/{userId}/favorite/photos/{favoritePhotoId}")
//    public Photo getFavoritePhoto(@PathVariable Long userId, @PathVariable Long favoritePhotoId) {
//        return photoService.getFavoritePhoto(userId,favoritePhotoId);
//    }

    @PostMapping(value = "/api/users/{userId}/favorite/photos")
    public ResponseEntity<?> addFavoritePhoto(@PathVariable Long userId, @RequestParam Long favoritePhotoId) {

        return validator.addFavoritePhoto(userId,favoritePhotoId);
    }

    @DeleteMapping(value = "/api/users/{userId}/favorite/photos/{favoritePhotoId}")
    public ResponseEntity<?> deleteFavoritePhoto(@PathVariable Long userId, @PathVariable Long favoritePhotoId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return validator.deleteFavoritePhoto(userId,favoritePhotoId);
    }



























}
