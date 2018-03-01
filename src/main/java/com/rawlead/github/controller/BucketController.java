package com.rawlead.github.controller;

import com.rawlead.github.service.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BucketController {

    private final AmazonClient amazonClient;

    @Autowired
    public BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/storage/uploadPhoto")
    public String uploadPhoto(@RequestPart(value = "file")MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }

    @DeleteMapping("/storage/deletePhoto")
    public String deletePhoto(@RequestPart(value = "url") String url) {
        return this.amazonClient.deleteFileFromS3Bucket(url);
    }
}
