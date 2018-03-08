package com.rawlead.github.controller;

import com.rawlead.github.entity.Comment;
import com.rawlead.github.entity.User;
import com.rawlead.github.service.CommentService;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {
    private CommentService commentService;
    private UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    private boolean isNotLoggedInUserMakesRequest(Long userId) {
        return !userId.equals(getLoggedUser().getId());
    }

    private User getLoggedUser() {
        return userService.getUser(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping(value = "/api/comments")
    public List<Comment> comments() {
        return this.commentService.listAllComments();
    }

    @GetMapping(value = "/api/comments/{commentId}")
    public Comment getComment(@PathVariable Long commentId) {
        return this.commentService.getComment(commentId);
    }

    @GetMapping(value = "/api/users/{userId}/comments")
    public Iterable<Comment> getUserComments(@PathVariable Long userId) {
        return this.commentService.listUserComments(userId);
    }

    @GetMapping(value = "/api/photos/{photoId}/comments")
    public Iterable<Comment> getCommentsToPhoto(@PathVariable Long photoId) {
        return this.commentService.listComments(photoId);
    }

    @PostMapping(value = "/api/photos/{photoId}/comments")
    public Comment addComment(@PathVariable Long photoId, @RequestParam String content) {

        Long userId = getLoggedUser().getId();
        return this.commentService.addComment(photoId, userId, content);


    }


    @DeleteMapping(value = "/api/comments")
    public boolean deleteAllComments() {
        return this.commentService.deleteAllComments();
    }

    @DeleteMapping(value = "/api/comments/{commentId}")
    public boolean deleteComment(@PathVariable Long commentId) {
        return this.commentService.deleteComment(commentId);
    }
}
