package com.spring.ecommerce.mongodb.repository;

import com.spring.ecommerce.mongodb.persistence.model.Banners;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BannerRepository extends MongoRepository<Banners, String> {
}
