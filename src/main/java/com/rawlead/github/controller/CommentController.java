package com.rawlead.github.controller;

import com.rawlead.github.entity.Comment;
import com.rawlead.github.entity.User;
import com.rawlead.github.service.CommentService;
import com.rawlead.github.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Comment addComment(@PathVariable Long photoId, @RequestBody String content) {
        Long userId = getLoggedUser().getId();
        return this.commentService.addComment(photoId, userId, content);
    }

    @DeleteMapping(value = "/api/comments")
    public ResponseEntity<?> deleteAllComments() {
        this.commentService.deleteAllComments();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/users/{userId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        if (isNotLoggedInUserMakesRequest(userId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        if (!this.commentService.deleteComment(userId, commentId))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
