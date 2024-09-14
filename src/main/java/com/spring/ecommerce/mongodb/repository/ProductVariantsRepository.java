package com.spring.ecommerce.mongodb.repository;

import com.spring.ecommerce.mongodb.persistence.model.ProductVariants;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductVariantsRepository extends MongoRepository<ProductVariants, String> {
}
