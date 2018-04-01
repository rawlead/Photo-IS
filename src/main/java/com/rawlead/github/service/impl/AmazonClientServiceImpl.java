package com.rawlead.github.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
// import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.rawlead.github.service.AmazonClientService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class AmazonClientServiceImpl implements AmazonClientService {
    private AmazonS3 s3client;

    @Value("${S3_ENDPOINT_URL}")
    private String endpointUrl;

    @Value("${S3_ACCESS_KEY}")
    private String accessKey;

    @Value("${S3_SECRET_KEY}")
    private String secretKey;

    @Value("${S3_BUCKET_NAME}")
    private String bucketName;

    @PostConstruct
    private void initAmazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,this.secretKey);
        // this.s3client = new AmazonS3Client(credentials);)
        this.s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.EU_CENTRAL_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
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


    @Override
    public boolean deleteFileFromS3Bucket(String url) {
        String name = url.substring(url.lastIndexOf("/") + 1);
        System.out.println(name);
        System.out.println(bucketName);
        s3client.deleteObject(new DeleteObjectRequest(bucketName,name));
        return true;
    }

    // save a file to S3 bucket and return fileUrl
    @Override
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
