package com.rawlead.github.service.impl;

import com.rawlead.github.entity.PhotoCategory;
import com.rawlead.github.repository.PhotoCategoryRepository;
import com.rawlead.github.service.PhotoCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoCategoryServiceImpl implements PhotoCategoryService {

    private PhotoCategoryRepository categoryRepository;

    @Autowired
    public PhotoCategoryServiceImpl(PhotoCategoryRepository photoCategoryRepository) {
        this.categoryRepository = photoCategoryRepository;
    }

    @Override
    public List<PhotoCategory> getAllCategories() {
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
}
