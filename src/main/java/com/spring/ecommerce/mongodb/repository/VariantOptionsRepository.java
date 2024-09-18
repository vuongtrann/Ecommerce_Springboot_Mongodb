package com.spring.ecommerce.mongodb.repository;

import com.spring.ecommerce.mongodb.persistence.model.variants.VariantOptions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VariantOptionsRepository extends MongoRepository<VariantOptions, String> {
}
