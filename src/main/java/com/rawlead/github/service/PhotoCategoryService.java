package com.rawlead.github.service;

import com.rawlead.github.entity.PhotoCategory;
import com.rawlead.github.repository.PhotoCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoCategoryService {

    private PhotoCategoryRepository categoryRepository;

    @Autowired
    public PhotoCategoryService(PhotoCategoryRepository photoCategoryRepository) {
        this.categoryRepository = photoCategoryRepository;
    }

    public List<PhotoCategory> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void saveCategory(String name) {
        PhotoCategory category = new PhotoCategory();
        category.setName(name);
        categoryRepository.save(category);
    }
}
