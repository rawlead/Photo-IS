package com.rawlead.github.service.impl;

import com.rawlead.github.entity.Comment;
import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.User;
import com.rawlead.github.repository.CommentRepository;
import com.rawlead.github.repository.PhotoRepository;
import com.rawlead.github.repository.UserRepository;
import com.rawlead.github.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PhotoRepository photoRepository;
    private UserRepository userRepository;


    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PhotoRepository photoRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Comment> listAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comment getComment(Long commentId) {
        return commentRepository.findOne(commentId);
    }

    @Override
    @Transactional
    public Comment addComment(Long photoId, Long userId, String content) {
        if (content.isEmpty())
            return null;
        User user = userRepository.findOne(userId);
        Photo photo = photoRepository.findOne(photoId);
        Comment comment = new Comment(LocalDateTime.now(), content, user, photo);
        if (!photo.addComment(comment))
            return null;
        if (!user.addComment(comment))
            return null;

//        photoRepository.save(photo);
//        userRepository.save(user);
        return commentRepository.save(comment);
//        return comment;
    }

    @Override
    public boolean deleteComment(Long userId, Long commentId) {
        Comment comment = commentRepository.findOne(commentId);
        System.out.println(comment.toString());
        if (comment == null)
            return false;
        System.out.println("deleting");
        commentRepository.delete(comment);
        return true;
    }

    @Override
    public Iterable<Comment> listComments(Long photoId) {
        return commentRepository.findCommentByPhotoIdOrderByDateCreated(photoId);
    }

    @Override
    public Iterable<Comment> listUserComments(Long userId) {
        return commentRepository.findCommentByUserIdOrderByDateCreated(userId);
    }

    @Override
    public boolean deleteAllComments() {
        List<User> users = userRepository.findAll();
        List<Photo> photos = photoRepository.findAll();
        for (User user : users)
            user.getComments().clear();
        for (Photo photo : photos)
            photo.getComments().clear();
        commentRepository.deleteAll();
        userRepository.save(users);
        photoRepository.save(photos);
        return true;
    }
}
