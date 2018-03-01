package com.rawlead.github.service;

import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.PhotoCategory;
import com.rawlead.github.entity.User;
import com.rawlead.github.repository.PhotoCategoryRepository;
import com.rawlead.github.repository.PhotoRepository;
import com.rawlead.github.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhotoService {

    private PhotoRepository photoRepository;
    private UserRepository userRepository;
    private PhotoCategoryRepository categoryRepository;
    private AmazonClient amazonClient;

    @Autowired
    public PhotoService(PhotoRepository photoRepository, UserRepository userRepository, PhotoCategoryRepository categoryRepository, AmazonClient amazonClient) {
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.amazonClient = amazonClient;
    }

    public List<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public void insert(Photo photo) {
        photoRepository.save(photo);
    }

    public List<Photo> findByUser(User user) {
        return photoRepository.findByUserId(user.getId());
    }

    public List<Photo> findByUser(Long userId) {
        return photoRepository.findByUserId(userId);
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


//    @Transactional
    public void postPhoto(Long userId, MultipartFile photo, String title, String description, String category) {
        User user = userRepository.findOne(userId);
        String url = amazonClient.uploadFile(photo);
        Photo newPhoto = new Photo();

        newPhoto.setTitle(title);
        newPhoto.setDescription(description);
        newPhoto.setPhotoCategory(categoryRepository.findByName(category));
        newPhoto.setUrl(url);
        newPhoto.setDateCreated(LocalDateTime.now());

        newPhoto.setUser(user);
        user.addPhoto(newPhoto);
        photoRepository.save(newPhoto);
        userRepository.save(user);
    }
}
