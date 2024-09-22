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
    List<Review>findAll();

@Aggregation(pipeline = {
        "{ $match: { _id: ?0 } }"
})
    public Optional<Review> findById(String id);


    @Aggregation(pipeline = {
            "{$match: { product: ObjectId(?0)}}",
            "{$project: {_id: 1, rating: 1, title: 1,description: 1, createdAt: 1 , customer: 1 }}",
            "{$sort: {rating:  -1, createdAt:  -1} }",
            "{$limit :  ?1}"
    })

    List<Review> findByProductId(String productId, int limit);



    @Aggregation(pipeline = {
            "{$match: {product: ObjectId(?0)}}",
            "{ $group: { _id: null, averageRating: { $avg: '$rating' } } }",
            "{$project: {averageRating: 1}}"
    })
    public double avgRating(String productId);







}
