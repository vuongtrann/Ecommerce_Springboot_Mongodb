package com.spring.ecommerce.mongodb.persistence.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AwsConfig {
    @Value("${aws.access.key.id}")
    private String accessKey;
    @Value("${aws.secret.access.key}")
    private String secretKey;
    @Value("${aws.region}")
    private String region;


    @Bean
    public TransferManager transferManager() {
        // Khởi tạo TransferManager với S3 client
        return TransferManagerBuilder.standard()
                .withS3Client(amazonS3())
                .withMultipartUploadThreshold((long) (5 * 1024 * 1024)) // Multipart upload cho file lớn hơn 5MB
                .withExecutorFactory(() -> Executors.newFixedThreadPool(10)) // 10 luồng song song
                .build();
    }
    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10); // Tùy chỉnh số lượng luồng
    }
    @Bean
    public AmazonS3 amazonS3() {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(region).build();
    }
}
