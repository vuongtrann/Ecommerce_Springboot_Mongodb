package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.dto.ProductForm;
import com.spring.ecommerce.mongodb.persistence.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductServices {
    public List<Product> findAll();
    public Product save(Product product);
    public Product update(Product product);
    public Optional<Product> findById(String id);

    public Product addProduct(ProductForm form);
}
