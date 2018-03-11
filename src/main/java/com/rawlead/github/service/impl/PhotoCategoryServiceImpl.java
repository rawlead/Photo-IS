package com.rawlead.github.service.impl;

import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.PhotoCategory;
import com.rawlead.github.repository.PhotoCategoryRepository;
import com.rawlead.github.repository.PhotoRepository;
import com.rawlead.github.service.PhotoCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoCategoryServiceImpl implements PhotoCategoryService {
    private PhotoRepository photoRepository;
    private PhotoCategoryRepository categoryRepository;

    @Autowired
    public PhotoCategoryServiceImpl(PhotoRepository photoRepository, PhotoCategoryRepository photoCategoryRepository) {
        this.photoRepository = photoRepository;
        this.categoryRepository = photoCategoryRepository;
    }

    @Override
    public List<PhotoCategory> listAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public PhotoCategory addCategory(String name) {
        PhotoCategory category = new PhotoCategory();
        category.setName(name);
        return categoryRepository.save(category);
    }

    @Override
    public PhotoCategory addCategory(PhotoCategory photoCategory) {
        return categoryRepository.save(photoCategory);
    }

    @Override
    public boolean deleteCategory(Long categoryId) {
        PhotoCategory category = categoryRepository.findOne(categoryId);
        if (category == null)
            return false;
        categoryRepository.delete(categoryId);
        return true;
    }

    @Override
    public List<Photo> getPhotosByCategory(Long categoryId) {
        return photoRepository.findByPhotoCategoryId(categoryId);
    }

    @Override
    public PhotoCategory getCategoryByName(String categoryName) {
        return categoryRepository.findByName(categoryName);
    }
}
