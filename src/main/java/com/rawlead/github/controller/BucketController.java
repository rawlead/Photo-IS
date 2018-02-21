package com.rawlead.github.controller;

import com.rawlead.github.service.AmazonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BucketController {

    private final AmazonClient amazonClient;

    @Autowired
    public BucketController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @PostMapping("/storage/uploadFile")
    public String uploadFile(@RequestPart(value = "file")MultipartFile file) {
        return this.amazonClient.uploadFile(file);
    }

    @DeleteMapping("/storage/deleteFile")
    public String deleteFile(@RequestPart(value = "url") String url) {
        return this.amazonClient.deleteFileFromS3Bucket(url);
    }
}
