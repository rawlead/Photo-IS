package com.rawlead.github.service;

import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.PhotoCategory;

import java.util.List;

public interface PhotoCategoryService {
    List<PhotoCategory> listAllCategories();

    PhotoCategory addCategory(String name);

    PhotoCategory addCategory(PhotoCategory photoCategory);

    boolean deleteCategory(Long categoryId);

    List<Photo> getPhotosByCategory(Long categoryId);

    PhotoCategory getCategoryByName(String categoryName);
}
