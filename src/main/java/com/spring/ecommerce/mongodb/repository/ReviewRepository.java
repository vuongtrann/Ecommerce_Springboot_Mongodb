package com.spring.ecommerce.mongodb.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import com.spring.ecommerce.mongodb.persistence.model.Review;

public interface ReviewRepository extends MongoRepository<Review, String> {
}
