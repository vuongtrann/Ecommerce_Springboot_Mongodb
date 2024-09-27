package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.Product;
import com.spring.ecommerce.mongodb.repository.CategoryRepository;
import com.spring.ecommerce.mongodb.repository.ProductRepository;
import com.spring.ecommerce.mongodb.services.CollectionServices;
import com.spring.ecommerce.mongodb.services.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CollectionServicesImpl implements CollectionServices {

    private final CategoryRepository categoryRepository;
    private final ProductServices productServices;


    @Override
    public List<Category> findAllCollections() {
        try {
            return categoryRepository.findCollections();
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    public Optional<Category> findCollectionById(String id) {
       try{
           return categoryRepository.findCollectionById(id);
       }catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }

    @Override
    public Category addCollection(Category collection) {
       try{
           collection.setCreatedAt(LocalDateTime.now());
           Category category = categoryRepository.save(collection);
           return category;
       }catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }







    @Override
    public Category updateCollection(Category collection) {
        collection.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(collection);
    }

    @Override
    public void deleteCollection(Category collection) {
        categoryRepository.delete(collection);
    }


    @Override
    public Product addProduct (String collectionId, String productId) {
        try{
            Product product = productServices.findById(productId).orElseThrow(()-> new RuntimeException("Product not found"));
            Category collection = categoryRepository.findCollectionById(collectionId).orElseThrow(()-> new RuntimeException("Collection not found"));
            product.getCollections().add(collection);
            return productServices.save(product);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



}
