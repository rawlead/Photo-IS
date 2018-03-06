package com.rawlead.github.controller;

import com.rawlead.github.service.AmazonClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BucketController {
    private AmazonClientService amazonClientService;

    @Autowired
    public BucketController(AmazonClientService amazonClientService) {
        this.amazonClientService = amazonClientService;
    }

    @PostMapping("/storage/uploadPhoto")
    public String uploadPhoto(@RequestPart(value = "file")MultipartFile file) {
        return this.amazonClientService.uploadFile(file);
    }

    @DeleteMapping("/storage/deletePhoto")
    public boolean deletePhoto(@RequestPart(value = "url") String url) {
        return this.amazonClientService.deleteFileFromS3Bucket(url);
    }
}
