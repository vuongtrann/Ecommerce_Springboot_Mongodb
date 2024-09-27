package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.Product;

import java.util.List;
import java.util.Optional;

public interface CollectionServices {
    public List<Category> findAllCollections();
    public Optional<Category> findCollectionById(String id);
    public Category addCollection(Category collection);
    public Category updateCollection(Category collection);
    public void deleteCollection(Category collection);
    public Product addProduct (String collectionId,String productId);


}
