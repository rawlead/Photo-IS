package com.rawlead.github.service;

import com.rawlead.github.entity.Comment;

import java.util.List;
import java.util.Set;

public interface CommentService {
    List<Comment> listAllComments();

    Comment getComment(Long commentId);

    Comment addComment( Long photoId,Long userId, String content);

    boolean deleteComment(Long userId, Long commentId);

    Iterable<Comment> listComments(Long photoId);

    Iterable<Comment> listUserComments(Long userId);

    boolean deleteAllComments();
}
