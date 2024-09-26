package com.spring.ecommerce.mongodb.repository;

import com.spring.ecommerce.mongodb.persistence.model.Specification.SpecificationTypes;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpecificationTypeRepository extends MongoRepository<SpecificationTypes,String> {
}
