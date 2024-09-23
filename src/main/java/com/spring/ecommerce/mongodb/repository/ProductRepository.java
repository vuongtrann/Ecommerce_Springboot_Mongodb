package com.spring.ecommerce.mongodb.repository;

import com.spring.ecommerce.mongodb.persistence.model.Product;
import com.spring.ecommerce.mongodb.persistence.projection.ProductProjection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    @Query(value = "{}", fields = "{ 'variants': 0, 'options': 0, 'dimensions': 0 }")
    List<ProductProjection> findAllProjected();

}
