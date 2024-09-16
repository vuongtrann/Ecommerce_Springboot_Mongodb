package com.spring.ecommerce.mongodb.services.Impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.spring.ecommerce.mongodb.services.S3Services;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service
public class S3ServicesImpl implements S3Services {

    private final AmazonS3 amazonS3;

    private final String bucketName;

    ObjectMetadata metadata = new ObjectMetadata();
    private static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/jpg");
    private static final int MAX_FILE_COUNT = 20; //10 Files
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; //10MB

    private final TransferManager transferManager;
    private ExecutorService executorService;

    public S3ServicesImpl(AmazonS3 amazonS3, TransferManager transferManager, @Value("${aws.s3.bucket.name}") String bucketName, ExecutorService executorService) {
        this.amazonS3 = amazonS3;
        this.transferManager = transferManager;
        this.bucketName = bucketName;

        this.executorService = executorService;
    }


//    public List<String> upload(String id, List<File> files) throws IOException {
//        if (files.size() > MAX_FILE_COUNT) { throw  new IllegalArgumentException("File count exceeds limit of " + MAX_FILE_COUNT); }
//        return files.stream().map(file -> {
//            try {
//                if (file.length() >= MAX_FILE_SIZE) { throw new IllegalArgumentException("File size exceeds limit of " + MAX_FILE_SIZE); }
//                String contentType = java.nio.file.Files.probeContentType(file.toPath());
//                if (contentType == null || !ALLOWED_FILE_TYPES.contains(contentType)) {
//                    throw new IllegalArgumentException("File " + file.getName() + " is not supported. Only " + ALLOWED_FILE_TYPES);
//                }
//
//                String fileKey = "product/" + id + "/" + file.getName().replaceAll("\\s+", "");
//                amazonS3.putObject(new PutObjectRequest(bucketName, fileKey, file).withCannedAcl(CannedAccessControlList.PublicRead));
//
//
//                return amazonS3.getUrl(bucketName, fileKey).toString();
//            } catch (Exception e) {
//                throw new RuntimeException("Error uploading file: " + file.getName(), e);
//            }
//        }).collect(Collectors.toList());
//    }
//
//    public List<String> upload(String id, List<File> files) throws IOException {
//        if (files.size() > MAX_FILE_COUNT) {
//            throw new IllegalArgumentException("File count exceeds limit of " + MAX_FILE_COUNT);
//        }
//
//        return files.stream().map(file -> {
//            try {
//                if (file.length() >= MAX_FILE_SIZE) {
//                    throw new IllegalArgumentException("File size exceeds limit of " + MAX_FILE_SIZE);
//                }
//
//                String contentType = java.nio.file.Files.probeContentType(file.toPath());
//                if (contentType == null || !ALLOWED_FILE_TYPES.contains(contentType)) {
//                    throw new IllegalArgumentException("File " + file.getName() + " is not supported. Only " + ALLOWED_FILE_TYPES);
//                }
//
//                // Tạo key cho file
//                String fileKey = "product/" + id + "/" + file.getName().replaceAll("\\s+", "");
//
//                // Bắt đầu Multipart upload
//                InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, fileKey)
//                        .withCannedACL(CannedAccessControlList.PublicRead);
//                InitiateMultipartUploadResult initResponse = amazonS3.initiateMultipartUpload(initRequest);
//
//                List<PartETag> partETags = new ArrayList<>();
//                long contentLength = file.length();
//                long partSize = 5 * 1024 * 1024; // 5 MB (kích thước tối thiểu của mỗi part)
//
//                try (FileInputStream inputStream = new FileInputStream(file)) {
//                    long filePosition = 0;
//                    for (int i = 1; filePosition < contentLength; i++) {
//                        partSize = Math.min(partSize, contentLength - filePosition);
//
//                        // Tạo request cho phần upload
//                        UploadPartRequest uploadRequest = new UploadPartRequest()
//                                .withBucketName(bucketName)
//                                .withKey(fileKey)
//                                .withUploadId(initResponse.getUploadId())
//                                .withPartNumber(i)
//                                .withFileOffset(filePosition)
//                                .withFile(file)
//                                .withPartSize(partSize);
//
//                        // Tải lên phần và lưu lại ETag để xác nhận sau này
//                        UploadPartResult uploadResult = amazonS3.uploadPart(uploadRequest);
//                        partETags.add(uploadResult.getPartETag());
//
//                        filePosition += partSize;
//                    }
//
//                    // Hoàn tất quá trình tải lên Multipart
//                    CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
//                            bucketName, fileKey, initResponse.getUploadId(), partETags);
//                    amazonS3.completeMultipartUpload(compRequest);
//
//                } catch (Exception e) {
//                    // Nếu có lỗi, hủy bỏ upload
//                    amazonS3.abortMultipartUpload(new AbortMultipartUploadRequest(
//                            bucketName, fileKey, initResponse.getUploadId()));
//                    throw new RuntimeException("Error uploading file: " + file.getName(), e);
//                }
//
//                // Trả về URL của file đã tải lên
//                return amazonS3.getUrl(bucketName, fileKey).toString();
//
//            } catch (Exception e) {
//                throw new RuntimeException("Error uploading file: " + file.getName(), e);
//            }
//        }).collect(Collectors.toList());
//    }

//    public List<String> upload(String id, List<File> files) throws IOException {
//        if (files.size() > MAX_FILE_COUNT) {
//            throw new IllegalArgumentException("File count exceeds limit of " + MAX_FILE_COUNT);
//        }
//
//        // Tạo ExecutorService để quản lý các luồng song song
//        ExecutorService executorService = Executors.newFixedThreadPool(10); // Tùy chỉnh số lượng luồng
//        List<Future<String>> futures = new ArrayList<>();
//
//        for (File file : files) {
//            futures.add(executorService.submit(() -> {
//                try {
//                    if (file.length() >= MAX_FILE_SIZE) {
//                        throw new IllegalArgumentException("File size exceeds limit of " + MAX_FILE_SIZE);
//                    }
//
//                    String contentType = java.nio.file.Files.probeContentType(file.toPath());
//                    if (contentType == null || !ALLOWED_FILE_TYPES.contains(contentType)) {
//                        throw new IllegalArgumentException("File " + file.getName() + " is not supported. Only " + ALLOWED_FILE_TYPES);
//                    }
//
//                    String fileKey = "product/" + id + "/" + file.getName().replaceAll("\\s+", "");
//
//                    // Bắt đầu Multipart upload
//                    InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, fileKey)
//                            .withCannedACL(CannedAccessControlList.PublicRead);
//                    InitiateMultipartUploadResult initResponse = amazonS3.initiateMultipartUpload(initRequest);
//
//                    List<PartETag> partETags = new ArrayList<>();
//                    long contentLength = file.length();
//                    long partSize = 5 * 1024 * 1024; // 5 MB
//
//                    try (FileInputStream inputStream = new FileInputStream(file)) {
//                        long filePosition = 0;
//                        for (int i = 1; filePosition < contentLength; i++) {
//                            partSize = Math.min(partSize, contentLength - filePosition);
//
//                            UploadPartRequest uploadRequest = new UploadPartRequest()
//                                    .withBucketName(bucketName)
//                                    .withKey(fileKey)
//                                    .withUploadId(initResponse.getUploadId())
//                                    .withPartNumber(i)
//                                    .withFileOffset(filePosition)
//                                    .withFile(file)
//                                    .withPartSize(partSize);
//
//                            UploadPartResult uploadResult = amazonS3.uploadPart(uploadRequest);
//                            partETags.add(uploadResult.getPartETag());
//
//                            filePosition += partSize;
//                        }
//
//                        CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(
//                                bucketName, fileKey, initResponse.getUploadId(), partETags);
//                        amazonS3.completeMultipartUpload(compRequest);
//
//                    } catch (Exception e) {
//                        amazonS3.abortMultipartUpload(new AbortMultipartUploadRequest(
//                                bucketName, fileKey, initResponse.getUploadId()));
//                        throw new RuntimeException("Error uploading file: " + file.getName(), e);
//                    }
//
//                    return amazonS3.getUrl(bucketName, fileKey).toString();
//
//                } catch (Exception e) {
//                    throw new RuntimeException("Error uploading file: " + file.getName(), e);
//                }
//            }));
//        }
//
//        // Đợi tất cả các luồng hoàn thành
//        List<String> urls = new ArrayList<>();
//        for (Future<String> future : futures) {
//            try {
//                urls.add(future.get()); // Nhận kết quả của từng luồng
//            } catch (Exception e) {
//                throw new RuntimeException("Error in parallel upload", e);
//            }
//        }
//
//        executorService.shutdown(); // Đảm bảo tắt executor sau khi hoàn thành
//        return urls;
//    }



    public List<String> upload(String id, List<File> files, String keyUrl) throws IOException {
        if (files.size() > MAX_FILE_COUNT) {
            throw new IllegalArgumentException("File count exceeds limit of " + MAX_FILE_COUNT);
        }

        List<Future<String>> futures = new ArrayList<>();
        for (File file : files) {
            futures.add(Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    if (file.length() >= MAX_FILE_SIZE) {
                        throw new IllegalArgumentException("File size exceeds limit of " + MAX_FILE_SIZE);
                    }

                    String contentType = java.nio.file.Files.probeContentType(file.toPath());
                    if (contentType == null || !ALLOWED_FILE_TYPES.contains(contentType)) {
                        throw new IllegalArgumentException("File " + file.getName() + " is not supported. Only " + ALLOWED_FILE_TYPES);
                    }

                    String fileKey = keyUrl + file.getName().replaceAll("\\s+", "");

                    // Tạo PutObjectRequest
                    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, file)
                            .withCannedAcl(CannedAccessControlList.PublicRead);

                    // Tải lên tệp sử dụng TransferManager
                    Upload upload = transferManager.upload(putObjectRequest);

                    // Đợi cho đến khi quá trình tải lên hoàn tất
                    upload.waitForCompletion();

                    return transferManager.getAmazonS3Client().getUrl(bucketName, fileKey).toString();

                } catch (Exception e) {
                    throw new RuntimeException("Error uploading file: " + file.getName(), e);
                }
            }));
        }

        // Đợi tất cả các luồng hoàn thành và thu thập kết quả
        List<String> urls = new ArrayList<>();
        for (Future<String> future : futures) {
            try {
                urls.add(future.get()); // Nhận kết quả URL của từng tệp
            } catch (Exception e) {
                throw new RuntimeException("Error in parallel upload", e);
            }
        }

        return urls;
    }



    @Override
    public String uploadBanner(MultipartFile file) throws IOException {
        String contentType = URLConnection.guessContentTypeFromName(file.getOriginalFilename().replaceAll("\\s+", ""));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(contentType != null ? contentType : "application/octet-stream");

        String fileKey = "banner/" + file.getOriginalFilename().replaceAll("\\s+", "");

        try {
            // Tạo PutObjectRequest với metadata
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata);

            // Tải lên tệp sử dụng TransferManager
            Upload upload = transferManager.upload(putObjectRequest);

            // Đợi cho đến khi quá trình tải lên hoàn tất
            upload.waitForCompletion();

            // Trả về URL của tệp đã tải lên
            return transferManager.getAmazonS3Client().getUrl(bucketName, fileKey).toString();

        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
        }
    }


//    public String uploadBanner( MultipartFile file) throws IOException {
//        String contenttype = URLConnection.guessContentTypeFromName(file.getOriginalFilename().replaceAll("\\s+", ""));
//        metadata.setContentLength(file.getSize());
//
//        if (contenttype != null){
//            metadata.setContentType(contenttype);
//        }
//        else {
//            metadata.setContentType("application/octet-stream");
//        }
//        String fileKey = "banner/" + file.getOriginalFilename().replaceAll("\\s+", "");
//        try {
//            amazonS3.putObject(new PutObjectRequest(bucketName, fileKey, file.getInputStream(), metadata));
//            return amazonS3.getUrl(bucketName, fileKey).toString();
//        }catch (Exception e){
//            throw new RuntimeException("Error uploading file: " + file.getOriginalFilename(), e);
//        }
//
//    }


    public String getFileKey(String url){
        int index = url.indexOf(".com/") + 5;
        return url.substring(index);
    }

    public void deleteImagesByUrls(List<String> imageUrls) {
        for (String url : imageUrls) {
            executorService.submit(() -> {
                try {
                    // Giải mã và lấy file key từ URL
                    String encodeFileKey = getFileKey(url);
                    String decodeFileKey = URLDecoder.decode(encodeFileKey, StandardCharsets.UTF_8);

                    // Tạo DeleteObjectRequest và xóa đối tượng từ S3
                    DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName, decodeFileKey);
                    amazonS3.deleteObject(deleteObjectRequest);
                    System.out.println("File deleted successfully: " + decodeFileKey);
                } catch (Exception e) {
                    System.err.println("Error deleting file: " + url + " - " + e.getMessage());
                }
            });
        }

        // Đợi cho tất cả các luồng hoàn tất
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Reinitialize the executor service if needed
        executorService = Executors.newFixedThreadPool(10);
    }

    public void remove(String url) {
        String encodeFileKey = getFileKey(url);
        String decodeFileKey = URLDecoder.decode(encodeFileKey, StandardCharsets.UTF_8);
        amazonS3.deleteObject(bucketName, decodeFileKey);

    }

//    public void deleteImagesByUrls(List<String> imageUrls) {
//        for (String imageUrl : imageUrls) {
//            String objectKey = URLDecoder.decode(getFileKey(imageUrl), StandardCharsets.UTF_8);
//            if (objectKey != null) {
//                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
//            }
//        }
//    }
}
