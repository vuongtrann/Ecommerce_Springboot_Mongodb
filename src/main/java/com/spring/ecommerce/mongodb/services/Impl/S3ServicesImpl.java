package com.spring.ecommerce.mongodb.services.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.spring.ecommerce.mongodb.services.S3Services;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class S3ServicesImpl implements S3Services {
    private final AmazonS3 amazonS3;

    private final String bucketName;

    ObjectMetadata metadata = new ObjectMetadata();
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg");
    private static final int MAX_FILE_COUNT = 10; //10 Files
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; //10MB


    public S3ServicesImpl(AmazonS3 amazonS3, @Value("${aws.s3.bucket.name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;

    }


    public List<String> upload(Long id, List<File> files) throws IOException {
        if (files.size() > MAX_FILE_COUNT) { throw  new IllegalArgumentException("File count exceeds limit of " + MAX_FILE_COUNT); }
        return files.stream().map(file -> {
            try {
                if (file.length() >= MAX_FILE_SIZE) { throw new IllegalArgumentException("File size exceeds limit of " + MAX_FILE_SIZE); }
                String contentType = java.nio.file.Files.probeContentType(file.toPath());
                if (contentType == null || !ALLOWED_FILE_TYPES.contains(contentType)) {
                    throw new IllegalArgumentException("File " + file.getName() + " is not supported. Only " + ALLOWED_FILE_TYPES);
                }

                String fileKey = "product/" + id + "/" + file.getName().replaceAll("\\s+", "");
                amazonS3.putObject(new PutObjectRequest(bucketName, fileKey, file).withCannedAcl(CannedAccessControlList.PublicRead));


                return amazonS3.getUrl(bucketName, fileKey).toString();
            } catch (Exception e) {
                throw new RuntimeException("Error uploading file: " + file.getName(), e);
            }
        }).collect(Collectors.toList());
    }



    public String uploadBanner( MultipartFile file) throws IOException {
        String contenttype = URLConnection.guessContentTypeFromName(file.getOriginalFilename().replaceAll("\\s+", ""));
        metadata.setContentLength(file.getSize());

        if (contenttype != null){
            metadata.setContentType(contenttype);
        }
        else {
            metadata.setContentType("application/octet-stream");
        }
        String fileKey = "banner/" + file.getOriginalFilename().replaceAll("\\s+", "");
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata));
            return amazonS3.getUrl(bucketName, fileKey).toString();
        }catch (Exception e){
            throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
        }

    }



    public String getFileKey(String url){
        int index = url.indexOf(".com/") + 5;

        return url.substring(index);
    }

    public void deleteImagesByUrls(List<String> imageUrls) {
        for (String imageUrl : imageUrls) {
            String objectKey = URLDecoder.decode(getFileKey(imageUrl), StandardCharsets.UTF_8);
            if (objectKey != null) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
            }
        }
    }

    public void remove(String url) {
        String encodeFileKey = getFileKey(url);
        String decodeFileKey = URLDecoder.decode(encodeFileKey, StandardCharsets.UTF_8);
        amazonS3.deleteObject(bucketName, decodeFileKey);

    }
}
