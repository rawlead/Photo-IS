package com.rawlead.github.controller;

import com.rawlead.github.entity.Photo;
import com.rawlead.github.entity.PhotoCategory;
import com.rawlead.github.service.PhotoCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PhotoCategoryController {
    private PhotoCategoryService categoryService;

    @Autowired
    public PhotoCategoryController(PhotoCategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping(value = "/api/categories")
    public List<PhotoCategory> listCategories() {
        return categoryService.listAllCategories();
    }

    @PostMapping(value = "/api/categories")
    public PhotoCategory addCategory(@RequestBody PhotoCategory photoCategory) {
        return categoryService.addCategory(photoCategory);
    }

    @GetMapping(value = "/api/categories/{categoryName}")
    public PhotoCategory getCategoryByName(@PathVariable String categoryName) {
        return categoryService.getCategoryByName(categoryName);
    }

    @GetMapping(value = "/api/categories/{categoryId}/photos")
    public List<Photo> getPhotosByCategory(@PathVariable Long categoryId) {
        return categoryService.getPhotosByCategory(categoryId);
    }

    @DeleteMapping(value = "/api/categories/{categoryId}")
    public boolean deleteCategory(@PathVariable Long categoryId) {
        return categoryService.deleteCategory(categoryId);
    }
}
