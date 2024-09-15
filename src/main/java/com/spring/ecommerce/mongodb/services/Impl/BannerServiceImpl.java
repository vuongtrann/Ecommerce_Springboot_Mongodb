package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.model.Banners;
import com.spring.ecommerce.mongodb.repository.BannerRepository;
import com.spring.ecommerce.mongodb.services.BannerService;
import com.spring.ecommerce.mongodb.services.S3Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class BannerServiceImpl implements BannerService {

    @Autowired
    private BannerRepository bannerRepository;

    @Autowired
    S3ServicesImpl s3Services;


    @Override
    public List<Banners> findAll() throws NullPointerException {
        return bannerRepository.findAll();
    }

    @Override
    public Optional<Banners> findById(String id) {
        Optional<Banners> banners = bannerRepository.findById(id);
        if (banners.isPresent()) {
            return banners;
        }
        return null;
    }

    @Override
    public Banners save(MultipartFile file, String categoryId) throws IOException {
        String url = s3Services.uploadBanner(file);
        Banners banners = new Banners();
        banners.setUrl(url);
        banners.setCategoryId(categoryId);
        banners.setStartDate(LocalDate.now());
        banners.setEndDate(LocalDate.now().plusDays(15));
        banners.setActive(true);
        bannerRepository.save(banners);
        return banners;
    }

    @Override
    public List<String> saveMultil(List<MultipartFile> files) {
        return files.stream().map(file -> {
            try {
                String url = s3Services.uploadBanner(file);
                Banners banners = new Banners();
                banners.setUrl(url);
                banners.setStartDate(LocalDate.now());
                banners.setEndDate(LocalDate.now().plusDays(10));
                bannerRepository.save(banners);
                return url;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());


    }

    @Override
    public Banners update(MultipartFile file, String id) throws IOException {
        try {
            Banners banners = bannerRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
            String url = s3Services.uploadBanner(file);
            s3Services.remove(banners.getUrl());
            banners.setUrl(url);
            bannerRepository.save(banners);

            return banners;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(String id) {
        try {
            String url = bannerRepository.findById(id).get().getUrl();
            s3Services.remove(url);
            bannerRepository.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Banners updateStatus(String id) throws NullPointerException {
        Banners banners = bannerRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        banners.setActive(!banners.isActive());
        return bannerRepository.save(banners);
    }


}



