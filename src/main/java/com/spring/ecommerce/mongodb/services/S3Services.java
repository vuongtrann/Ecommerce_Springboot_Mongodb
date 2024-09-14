package com.spring.ecommerce.mongodb.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface S3Services {
    public List<String> upload(Long id, List<File> files) throws IOException;
    public String uploadBanner( MultipartFile file) throws IOException;
    public void deleteImagesByUrls(List<String> imageUrls);
    public void remove(String url);

}
