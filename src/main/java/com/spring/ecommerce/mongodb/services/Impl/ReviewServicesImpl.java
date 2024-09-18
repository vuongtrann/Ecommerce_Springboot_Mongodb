package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.model.Review;
import com.spring.ecommerce.mongodb.repository.ReviewRepository;
import com.spring.ecommerce.mongodb.services.ReviewServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ReviewServicesImpl implements ReviewServices {
    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> findAll() {
        try {
            return reviewRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public Review findById(String id) {
        try {
            return reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("Review not found"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Review save(Review review) {
        try {
            return reviewRepository.save(review);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteById(String id) {
        try{
            reviewRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Review update(String id,Review review) {
        try {
            Review oldReview = findById(id);
            if (review.getRating() != 0) {
                oldReview.setRating(review.getRating());
            }
            if (review.getTitle() != null) {
                oldReview.setTitle(review.getTitle());
            }
            if (review.getDescription() != null) {
                oldReview.setDescription(review.getDescription());
            }
            return reviewRepository.save(oldReview);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Review> findByProductId(String productId) {
        return List.of();
    }
}
