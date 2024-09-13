package com.spring.ecommerce.mongodb.repository;

import org.springframework.boot.Banner;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BannerRepository extends MongoRepository<Banner, String> {
}
