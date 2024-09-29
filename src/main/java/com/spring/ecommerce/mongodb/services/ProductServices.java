package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.dto.ProductForm;
import com.spring.ecommerce.mongodb.persistence.model.Product;
import com.spring.ecommerce.mongodb.persistence.projection.ProductProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductServices {
    public Page<ProductProjection> findAll(Pageable pageable);
    public List<Product> findAllPopularProducts(int limit);
    public Product save(Product product);
    //public Product update(Product product);
    public Optional<Product> findById(String id);
    public Product addProduct(ProductForm form);
    public Product updateProduct(String productId, ProductForm form);

    //public Product addProduct(Product form);


}
