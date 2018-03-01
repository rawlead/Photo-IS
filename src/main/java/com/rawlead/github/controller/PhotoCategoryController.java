package com.rawlead.github.controller;

import com.rawlead.github.entity.PhotoCategory;
import com.rawlead.github.service.PhotoCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return categoryService.getAllCategories();
    }



}
