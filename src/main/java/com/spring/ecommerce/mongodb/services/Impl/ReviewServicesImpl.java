package com.spring.ecommerce.mongodb.services.Impl;

import com.mongodb.MongoException;
import com.spring.ecommerce.mongodb.persistence.dto.ReviewForm;
import com.spring.ecommerce.mongodb.persistence.model.ListStar;
import com.spring.ecommerce.mongodb.persistence.model.Product;
import com.spring.ecommerce.mongodb.persistence.model.Review;
import com.spring.ecommerce.mongodb.repository.ReviewRepository;
import com.spring.ecommerce.mongodb.services.ReviewServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class ReviewServicesImpl implements ReviewServices {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    ProductServicesImpl productServices;

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
            Review review = reviewRepository.findById(id).get();
            return review;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Review save(Review review) {
        try {
            if (review.getRating() > 5){
                throw new MongoException("Rating is greater than 5");
            }
            review.setCreatedAt(LocalDateTime.now());
            reviewRepository.save(review);
            productServices.updateRating(review.getProduct().getId(), reviewRepository.avgRating(review.getProduct().getId()));
            return review;
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
    public Review update(Review review) {
        try {

            Review oldReview = reviewRepository.findById(review.getId()).orElseThrow(()-> new RuntimeException("Review not found"));
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
    public List<ReviewForm> findByProductId(String productId, ListStar stars ,String sortBy, String direction, int limit ) {
        try{
            int order =1;
            if (direction.equals("DESC")){
                order=-1;
            }
            List<ReviewForm> forms = reviewRepository.findByProductId(productId, stars.getStars() , sortBy ,order ,limit);

            return  forms;
//            return  reviewRepository.findByProductId(productId, limit);
        }catch (Exception e){
            throw new MongoException(e.getMessage());
        }

    }
}
