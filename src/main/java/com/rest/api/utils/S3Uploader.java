package com.rest.api.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class S3Uploader {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;


    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    public String upload(MultipartFile multipartFile) throws IOException {
        //
        StringBuffer s3FilePath = new StringBuffer();
        String newFileName = new Random().nextInt( 999999 ) + "_" + System.currentTimeMillis() + multipartFile.getOriginalFilename().substring( multipartFile.getOriginalFilename().lastIndexOf( "." ) );
        //
        String fileContentType = multipartFile.getContentType();
        if(fileContentType.startsWith("image/")){
            s3FilePath.append("images/");
        } else {
            s3FilePath.append("datas/");
        }
        s3FilePath.append(makeDirectoryName());

        return putToS3(multipartFile, s3FilePath.toString(), newFileName);
    }

    private String makeDirectoryName(){
        LocalDateTime today = LocalDateTime.now();
        return today.getYear() +
                "/" +
                today.getMonthValue() +
                "/" +
                today.getDayOfMonth() +
                "/" +
                today.getHour() +
                "/";
    }

    private String putToS3(MultipartFile file, String filePath, String fileName) throws IOException{

        s3Client.putObject(
                new PutObjectRequest("ones.cloud", filePath + fileName, file.getInputStream(), null)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return "https://ones.cloud/" + filePath + fileName;
    }
}
