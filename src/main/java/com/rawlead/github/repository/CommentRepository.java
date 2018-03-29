package com.rawlead.github.repository;

import com.rawlead.github.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Iterable<Comment> findCommentByUserIdOrderByDateCreated(Long userId);

    Iterable<Comment> findCommentByPhotoIdOrderByDateCreated(Long photoId);

    Iterable<Comment> findCommentByPhotoId(Long photoId);

    void deleteCommentByPhotoId(Long photoId);

}
