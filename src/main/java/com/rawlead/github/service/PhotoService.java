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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public boolean deletePhoto(Long id) {
        Photo photo = photoRepository.findOne(id);
        if (photo == null)
            return false;
        amazonClient.deleteFileFromS3Bucket(photo.getUrl());
        photoRepository.delete(photo);
        return true;
    }

    public void deletePhoto(Long userId, Long photoId) {
        User user = userRepository.findOne(userId);
        Photo photo = photoRepository.findOne(photoId);
        photoRepository.delete(photoId);
    }

    public Photo getPhoto(Long id) {
        return photoRepository.findOne(id);
    }

    //    @Transactional
    public void addPhoto(Long userId, MultipartFile photo, String title, String description, String category) {
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

    public Set<Photo> getFavoritePhotos(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null)
            return new HashSet<>();
        return userRepository.findOne(userId).getFavoritePhotos();
    }

    public boolean addFavoritePhoto(Long userId, Long favoritePhotoId) {
        User user = userRepository.findOne(userId);
        Photo favoritePhoto = photoRepository.findOne(favoritePhotoId);
        if (user == null || favoritePhoto == null)
            return false;
        if (!user.addFavoritePhoto(favoritePhoto))
            return false;
        if (!favoritePhoto.addUserFavorite(user))
            return false;
        userRepository.save(user);
        photoRepository.save(favoritePhoto);
        return true;
    }

    public boolean deleteFavoritePhoto(Long userId, Long favoritePhotoId) {
        User user = userRepository.findOne(userId);
        Photo favoritePhoto = photoRepository.findOne(favoritePhotoId);
        if (user == null || photoRepository == null)
            return false;
        if (!user.deleteFavoritePhoto(favoritePhoto))
            return false;
        if (!favoritePhoto.deleteUserFavorite(user))
            return false;
        userRepository.save(user);
        photoRepository.save(favoritePhoto);
        return true;
    }

//    public Photo getFavoritePhoto(Long userId, Long favoritePhotoId) {
//        User user = userRepository.findOne(userId);
//        Photo favoritePhote = photoRepository.findOne(favoritePhotoId);
//        if (user.hasFavoritePhoto(favoriteUser))
//            return favoriteUser;
//        return null;
//    }


}
