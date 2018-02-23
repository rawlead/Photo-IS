package com.rawlead.github.service;

import com.rawlead.github.entity.PhotoObject;
import com.rawlead.github.entity.User;
import com.rawlead.github.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<PhotoObject>  gerAllPosts() {
        return postRepository.findAll();
    }

    public void insert(PhotoObject photoObject) {
        postRepository.save(photoObject);
    }

    public List<PhotoObject> findByUser(User user) {
        return postRepository.findByCreatorId(user.getId());
    }

    public boolean deletePost(Long id) {
        PhotoObject photoObject = postRepository.findOne(id);
        if (photoObject == null)
            return false;
        postRepository.delete(photoObject);
        return true;
    }

    public PhotoObject getPost(Long id) {
        return postRepository.findOne(id);
    }


}
