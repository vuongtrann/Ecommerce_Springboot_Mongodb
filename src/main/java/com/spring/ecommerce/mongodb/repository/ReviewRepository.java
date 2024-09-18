package com.spring.ecommerce.mongodb.repository;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.spring.ecommerce.mongodb.persistence.model.Review;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    @Aggregation(pipeline = {
            "{$match: {}}"
    })
    List<Review> findAll();


    @Aggregation(pipeline = {
            "{$match: {_id:  ?0}}"
    })
    public Optional<Review> findById(String id);



    List<Review> findByProductId(String productId);
}
