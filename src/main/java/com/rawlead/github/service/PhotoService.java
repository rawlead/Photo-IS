package com.rawlead.github.service;

import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.User;
import com.rawlead.github.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoService {

    private PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public List<Photo>  gerAllPosts() {
        return photoRepository.findAll();
    }

    public void insert(Photo photo) {
        photoRepository.save(photo);
    }

    public List<Photo> findByUser(User user) {
        return photoRepository.findByUserId(user.getId());
    }

    public boolean deletePost(Long id) {
        Photo photo = photoRepository.findOne(id);
        if (photo == null)
            return false;
        photoRepository.delete(photo);
        return true;
    }

    public Photo getPost(Long id) {
        return photoRepository.findOne(id);
    }


}
