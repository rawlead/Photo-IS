package com.rawlead.github.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonClient {
    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @PostConstruct
    private void initAmazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    // AWS upload requires File parameter
    private File multipartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convertedFile);
        fos.write(file.getBytes());
        fos.close();
        return convertedFile;
    }

    // Generate a new name for a file and replace spaces with "-"
    private String nameGenerator(MultipartFile multipartFile) {
        return new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "-");
    }


    private void uploadFileTOS3Bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName,fileName,file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
    public String deleteFileFromS3Bucket(String url) {
        String name = url.substring(url.lastIndexOf("/") + 1);
        System.out.println(name);
        System.out.println(bucketName);
        s3client.deleteObject(new DeleteObjectRequest(bucketName,name));
        return "File deleted";
    }


    // save a file to S3 bucket and return fileUrl
    //TODO fileUrl store to database
    public String uploadFile(MultipartFile multipartFile) {
        String url = "";
        try {
            File file = multipartToFile(multipartFile);
            String name = nameGenerator(multipartFile);
            url = endpointUrl + "/" + bucketName + "/" + name;
            uploadFileTOS3Bucket(name,file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }


}
