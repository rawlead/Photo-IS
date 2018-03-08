package com.rawlead.github.service;

import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface PhotoService {
    List<Photo> listAllPhotos();

    List<Photo> findByUser(Long userId);

    boolean deletePhoto(Long id);

    Photo getPhoto(Long id);

    Photo addPhoto(Long userId, MultipartFile photo, String title, String description, String category);

    Set<Photo> listFavoritePhotos(Long userId);

    boolean addFavoritePhoto(Long userId, Long favoritePhotoId);

    boolean deleteFavoritePhoto(Long userId, Long favoritePhotoId);

}
