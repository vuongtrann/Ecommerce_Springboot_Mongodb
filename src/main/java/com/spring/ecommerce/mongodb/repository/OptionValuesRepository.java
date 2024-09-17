package com.spring.ecommerce.mongodb.repository;

import com.spring.ecommerce.mongodb.persistence.model.variants.OptionValues;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OptionValuesRepository extends MongoRepository<OptionValues,String> {
}
