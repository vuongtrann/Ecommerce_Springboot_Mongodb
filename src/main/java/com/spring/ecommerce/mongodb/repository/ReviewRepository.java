package com.spring.ecommerce.mongodb.repository;
import com.spring.ecommerce.mongodb.persistence.dto.ReviewForm;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.spring.ecommerce.mongodb.persistence.model.Review;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    @Aggregation(pipeline = {
            "{$match: { rating: {$in: ?0}}}"
    })
    List<Review>findAll(int[] a);

@Aggregation(pipeline = {
        "{ $match: { _id: ?0 } }"
})
    public Optional<Review> findById(String id);


    @Aggregation(pipeline = {
            "{$match: {product : ObjectId(?0),   rating: {$in: ?1}   }}"

            ,"{$lookup: {from: 'product', localField: 'product', foreignField: '_id', as: 'productsDetail'}}"
            ,"{$unwind: '$productsDetail'}"

            ,"{$lookup: {from: 'variant_options', localField: 'productsDetail.variants', foreignField: '_id', as: 'variantDetails'}}"

            ,"{$lookup: {from: 'customer', localField: 'customer', foreignField: '_id', as: 'customerDetails'}}"
            ,"{$unwind: '$customerDetails'}"

            ,"{$project: {_id: 1, 'rating': 1, 'title': 1, 'description': 1, 'createdAt': 1"
            + ",'productsDetail._id': 1, 'productsDetail.name': 1"
            + ",'variantDetails': { $map: { input: '$variantDetails', as: 'variant', in: { 'variantTypes': '$$variant.variantTypes', 'value': '$$variant.value' } } }"
            + ",'customerDetails._id': 1, 'customerDetails.fullName': 1 }}"

            ,"{$sort: {?2 :  ?3} }"
            ,"{$limit: ?4}"
    })
    List<ReviewForm> findByProductId(String productId,List<Integer> stars ,String sortBy, int order , int limit   );
//    List<ReviewForm> findByProductId(String productId, int limit);



    @Aggregation(pipeline = {
            "{$match: {product: ObjectId(?0)}}",
            "{ $group: { _id: null, averageRating: { $avg: '$rating' } } }",
            "{$project: {averageRating: 1}}"
    })
    public double avgRating(String productId);







}
