package com.rawlead.github.controller;

import com.rawlead.github.configuration.CustomUserDetails;
import com.rawlead.github.entity.Photo;
import com.rawlead.github.service.PhotoService;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
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
        return photoService.gerAllPosts();
    }

    @PostMapping(value = "/api/photos")
    public String publishPost(@RequestBody Photo photo) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (photo.getDateCreated() == null)
            photo.setDateCreated(LocalDateTime.now());
        photo.setUser(userService.getUser(userDetails.getUsername()));
        photoService.insert(photo);
        return "Photo was published";
    }

    @GetMapping(value = "/api/users/{username}/photos")
    public List<Photo> postsByUser(@PathVariable String username) {
        return photoService.findByUser(userService.getUser(username));
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
