package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.model.Banners;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BannerService {
    public List<Banners> findAll();
    public Optional<Banners> findById(String id);
    public Banners save(MultipartFile file, String categoryId) throws IOException;
    public List<Banners> saveMultil ( List<MultipartFile> files);
    public Banners update(MultipartFile file, String idBanner) throws IOException;
    public void delete(String id);
    public Banners updateStatus(String id);

}