package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.model.Review;

import java.util.List;

public interface ReviewServices {
    public List<Review> findAll();
    public Review findById(String id);
    public Review save(Review review);
    public void deleteById(String id);
    public Review update(String id ,Review review);
    public List<Review> findByProductId(String productId);
}
