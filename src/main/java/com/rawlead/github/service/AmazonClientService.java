package com.rawlead.github.service;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonClientService {
    boolean deleteFileFromS3Bucket(String url);

    String uploadFile(MultipartFile multipartFile);
}
