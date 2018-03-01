package com.rawlead.github.controller;

import com.rawlead.github.entity.Photo;
import com.rawlead.github.service.PhotoService;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class PortalController {
    private PhotoService photoService;
    private UserService userService;

    @Autowired
    public PortalController(PhotoService photoService, UserService userService) {
        this.photoService = photoService;
        this.userService = userService;
    }

    @GetMapping(value = "/private")
    public String privateArea() {
        return "private";
    }

    @GetMapping(value = "/api/photos")
    public List<Photo> posts() {
        return photoService.getAllPhotos();
    }

    @GetMapping(value = "/api/users/{userId}/photos")
    public List<Photo> postsByUser(@PathVariable Long userId) {
        return photoService.findByUser(userId);
    }

    @PostMapping(value = "/api/users/{userId}/photos")
    public ResponseEntity<?> postPhoto(@RequestPart MultipartFile photo,
                                       @RequestPart(required = false) String title,
                                       @RequestPart(required = false) String description,
                                       @RequestPart String category,
                                       @PathVariable Long userId) {
        System.out.println("title: " + title + "; description: " + description + "; category: " + category);
        this.photoService.postPhoto(userId,photo,title,description,category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/photos/{id}")
    public boolean delete(@PathVariable Long id) {
        return photoService.deletePost(id);
    }


    //TODO THIS IS SHIT
    @GetMapping(value = "/api/the_post/{id}")
    public Photo getPostById(@PathVariable Long id) {
        return photoService.getPost(id);
    }
}
