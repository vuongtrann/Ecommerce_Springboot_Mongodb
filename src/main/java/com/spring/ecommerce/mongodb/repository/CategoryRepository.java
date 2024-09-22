package com.spring.ecommerce.mongodb.repository;


import com.spring.ecommerce.mongodb.persistence.model.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {

    @Aggregation(pipeline = {
            " {$match: { isCollection: false, isFeature: false, isActive: true}}",
            "{$project: {_id: 1, name:  1}}"
    })
    List<Category> findAll();

    @Aggregation(pipeline = {
            "{$match: { _id: ?0}}"
    })
    Optional<Category> findByIdCategory(@Param("id") String id);


    @Aggregation(pipeline = {
            "{$lookup:{from: 'product', localField: '_id', foreignField: 'categories', as:  'product'}}"
            ,"{$unwind: '$product'}"
            ,"{$group: { _id: '$_id', " +
                    "noOfViews: { $sum: {$ifNull:['$product.viewCount' , 0]}}" +
                     ", noOfSold: { $sum: {$ifNull:  ['$product.quantitySold',0]} }" +
                    ", name: { $first: '$name' }, icon: { $first: '$icon' } } } "
            ,"{$project:{ id: '$_id' ,icon: 1, name: 1, noOfViews: '$noOfViews', noOfSold: '$noOfSold' } }"
            , "{$sort:  {noOfSold: -1,noOfViews: -1} }"
            ,"{$limit: ?0}"
    })
    List<Category> findTopCategorie(int limit);



    @Aggregation(pipeline = {
            "{$match:  {isCollection:  true}}"
    })
    List<Category> findCollections();


    /** Update Parent Category after delete*/
    @Modifying
    @Transactional
    @Query(value = "{'subCategories': ?0}")
    @Update ( value = "{'$pull': {'subCategories': ?0}}" )
    void updateParents(String subCategoryId);
}
