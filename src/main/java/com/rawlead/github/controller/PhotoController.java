package com.rawlead.github.controller;

import com.rawlead.github.pojo.ResponseMessage;
import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.User;
import com.rawlead.github.service.*;
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

    @Autowired
    public PhotoController(PhotoService photoService, UserService userService ) {
        this.photoService = photoService;
        this.userService = userService;
    }

    private boolean isNotLoggedInUserMakesRequest(Long userId) {
        return !userId.equals(getLoggedUser().getId());
    }

    private User getLoggedUser() {
        return userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping(value = "/api/photos")
    public List<Photo> photos() {
        return photoService.listAllPhotos();
    }

    @GetMapping(value = "/api/photos/{photoId}")
    public Photo getPhoto(@PathVariable Long photoId) {
        return photoService.getPhoto(photoId);
    }

    @GetMapping(value = "/api/photos/{photoId}/favorite")
    public Set<User> gerFavoriteOfUsers(@PathVariable Long photoId) {
        return photoService.listFavoriteOfUsers(photoId);
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
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        this.photoService.addPhoto(userId, photo, title, description, category);
        return new ResponseEntity<>(HttpStatus.OK);
    }




    @DeleteMapping(value = "/api/users/{userId}/photos/{photoId}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long userId, @PathVariable Long photoId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (this.photoService.deletePhoto(photoId))
            return new ResponseEntity<>(ResponseMessage.PHOTO_DELETED, HttpStatus.OK);
        return new ResponseEntity<>(ResponseMessage.PHOTO_NOT_FOUND, HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/api/users/{userId}/favorite/photos")
    public Set<Photo> getFavoritePhotos(@PathVariable Long userId) {
        return photoService.listFavoritePhotos(userId);
    }

    @PostMapping(value = "/api/users/{userId}/favorite/photos")
    public ResponseEntity<?> addFavoritePhoto(@PathVariable Long userId, @RequestParam Long favoritePhotoId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (photoService.addFavoritePhoto(userId, favoritePhotoId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(ResponseMessage.PHOTO_ALREADY_FAVORITE_OR_DOESNT_EXIST, HttpStatus.CONFLICT);
    }

    @DeleteMapping(value = "/api/users/{userId}/favorite/photos/{favoritePhotoId}")
    public ResponseEntity<?> deleteFavoritePhoto(@PathVariable Long userId, @PathVariable Long favoritePhotoId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (photoService.deleteFavoritePhoto(userId, favoritePhotoId))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(ResponseMessage.PHOTO_ALREADY_FAVORITE_OR_DOESNT_EXIST, HttpStatus.CONFLICT);

    }


}
