package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.Product;

import java.util.List;
import java.util.Optional;

public interface CategoryServices {
    public List<Category> findAll();
    public Optional<Category> findById(String id);
    public Category save(Category category);
    public void delete(String id);
    public Category update(Category category);
}
