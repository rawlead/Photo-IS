package com.rawlead.github.controller;

import com.rawlead.github.configuration.CustomUserDetails;
import com.rawlead.github.entity.PhotoObject;
import com.rawlead.github.service.PostService;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class PortalController {
    private PostService postService;
    private UserService userService;

    @Autowired
    public PortalController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping(value = "/private")
    public String privateArea() {
        return "private";
    }

    @GetMapping(value = "/api/posts")
    public List<PhotoObject> posts() {
        return postService.gerAllPosts();
    }

    @PostMapping(value = "/api/posts")
    public String publishPost(@RequestBody PhotoObject photoObject) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (photoObject.getDateCreated() == null)
            photoObject.setDateCreated(new Date());
        photoObject.setCreator(userService.getUser(userDetails.getUsername()));
        postService.insert(photoObject);
        return "Photo was published";
    }

    @GetMapping(value = "/api/users/{username}/posts")
    public List<PhotoObject> postsByUser(@PathVariable String username) {
        return postService.findByUser(userService.getUser(username));
    }

    @DeleteMapping(value = "/api/posts/{id}")
    public boolean delete(@PathVariable Long id) {
        return postService.deletePost(id);
    }


    //TODO THIS IS SHIT
    @GetMapping(value = "/api/the_post/{id}")
    public PhotoObject getPostById(@PathVariable Long id) {
        return postService.getPost(id);
    }
}
