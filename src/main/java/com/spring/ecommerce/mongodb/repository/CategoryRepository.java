package com.spring.ecommerce.mongodb.repository;


import com.spring.ecommerce.mongodb.persistence.dto.CollectionForm;
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


    /** Get all Category */
    @Aggregation(pipeline = {
            " {$match: { isCollection: false, isFeature: false, isActive: true}}",
            "{$project: {_id: 1, name:  1,icon: 1, banner: 1 }}"
    })
    List<Category> findAll();



    /** Get  Category By Id */
    @Aggregation(pipeline = {
            "{$match: { _id: ?0}}"
    })
    Optional<Category> findByIdCategory(@Param("id") String id);

    /** Get TOP  Category */
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



    /** Update Parent Category after delete*/
    @Modifying
    @Transactional
    @Query(value = "{'subCategories': ?0}")
    @Update ( value = "{'$pull': {'subCategories': ?0}}" )
    void updateParents(String subCategoryId);




    /** Get sub Category By Id of Parent  */

    @Aggregation(pipeline = {
            "{ $match: { _id: ?0 } }"
            ,"{ $lookup: {from: 'category', localField: 'subCategories', foreignField: '_id', as: 'subCategoriesDetails' } }"
            ,"{$unwind: '$subCategoriesDetails'  }"
            ,"{ $project: { id: '$subCategoriesDetails._id', name: '$subCategoriesDetails.name', createdAt: '$subCategoriesDetails.createdAt', " +
                            "updatedAt: '$subCategoriesDetails.updatedAt', icon: '$subCategoriesDetails.icon', banner: '$subCategoriesDetails.banner', " +
                            "variantTypes: '$subCategoriesDetails.variantTypes', noOfViews: '$subCategoriesDetails.noOfViews'," +
                            " noOfSold: '$subCategoriesDetails.noOfSold', active: '$subCategoriesDetails.isActive',    " +
                            " feature: '$subCategoriesDetails.isFeature', collection: '$subCategoriesDetails.isCollection'" +
//                                ", subCategories: '$subCategoriesDetails.subCategories' " +
            "} }"

    })
    public List<Category> findSubCategory(String id);








    /** Get all Collections */
    @Aggregation(pipeline = {
            "{$match:  {isCollection:  true}}"
    })
    List<Category> findCollections();



    /** Get  Collections By Id  */
    @Aggregation(pipeline = {
            " {$match: { _id:  ?0 ,isCollection: true, isFeature: false, isActive: true}}",
//            "{$project: {_id: 1, name:  1}}"
    })
    Optional<Category > findCollectionById(String id);



    /** Get Product by Collection Id*/
        @Aggregation(pipeline = {
               " {$match: {_id:  ObjectId(?0)}}"
                ,"{$lookup: {from: 'product', localField: '_id', foreignField: 'collections', as: 'productDetails'} }"
                ,"{$addFields: {total: {$size: '$productDetails'}}}"
//                ,"{$unwind: '$productDetails'}"
    //            ,"{$project : {_id: 1, name: 1, total: {$size: '$productDetails' }, productDetails: { _id': 1 , 'productDetails': {_id: 1, 'name': 1 ,rating: 1 }}
                ,"{$project: { _id: 1, name: 1, total: 1, productDetails: 1 }}"

        })
        CollectionForm findProductDetails(String id);

}

