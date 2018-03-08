package com.rawlead.github.service.impl;

import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.User;
import com.rawlead.github.repository.PhotoCategoryRepository;
import com.rawlead.github.repository.PhotoRepository;
import com.rawlead.github.repository.UserRepository;
import com.rawlead.github.service.AmazonClientService;
import com.rawlead.github.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PhotoServiceImpl implements PhotoService {
    private PhotoRepository photoRepository;
    private UserRepository userRepository;
    private PhotoCategoryRepository categoryRepository;
    private AmazonClientService amazonClientService;

    @Autowired
    public PhotoServiceImpl(PhotoRepository photoRepository, UserRepository userRepository, PhotoCategoryRepository categoryRepository, AmazonClientService amazonClientService) {
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.amazonClientService = amazonClientService;
    }

    @Override
    public List<Photo> listAllPhotos() {
        return photoRepository.findAll();
    }

    @Override
    public List<Photo> findByUser(Long userId) {
        return photoRepository.findByUserId(userId);
    }

    @Override
    public boolean deletePhoto(Long id) {
        Photo photo = photoRepository.findOne(id);
        if (photo == null)
            return false;
        amazonClientService.deleteFileFromS3Bucket(photo.getUrl());
        photoRepository.delete(photo);
        return true;
    }

    @Override
    public Photo getPhoto(Long id) {
        return photoRepository.findOne(id);
    }

    @Override
    public Photo addPhoto(Long userId, MultipartFile photo, String title, String description, String category) {
        User user = userRepository.findOne(userId);
        String url = amazonClientService.uploadFile(photo);
        Photo newPhoto = new Photo();

        newPhoto.setTitle(title);
        newPhoto.setDescription(description);
        newPhoto.setPhotoCategory(categoryRepository.findByName(category));
        newPhoto.setUrl(url);
        newPhoto.setDateCreated(LocalDateTime.now());

        newPhoto.setUser(user);
        user.addPhoto(newPhoto);
        userRepository.save(user);
        return photoRepository.save(newPhoto);

    }

    @Override
    public Set<Photo> listFavoritePhotos(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null)
            return new HashSet<>();
        return userRepository.findOne(userId).getFavoritePhotos();
    }

    @Override
    public boolean addFavoritePhoto(Long userId, Long favoritePhotoId) {
        User user = userRepository.findOne(userId);
        Photo favoritePhoto = photoRepository.findOne(favoritePhotoId);
        if (user == null || favoritePhoto == null)
            return false;
        if (!user.addFavoritePhoto(favoritePhoto))
            return false;
        userRepository.save(user);
        photoRepository.save(favoritePhoto);
        return true;
    }

    @Override
    public boolean deleteFavoritePhoto(Long userId, Long favoritePhotoId) {
        User user = userRepository.findOne(userId);
        Photo favoritePhoto = photoRepository.findOne(favoritePhotoId);
        if (user == null || photoRepository == null)
            return false;
        if (!user.deleteFavoritePhoto(favoritePhoto))
            return false;
        userRepository.save(user);
        photoRepository.save(favoritePhoto);
        return true;
    }

}
