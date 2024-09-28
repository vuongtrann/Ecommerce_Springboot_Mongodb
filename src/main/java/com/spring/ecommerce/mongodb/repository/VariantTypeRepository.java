package com.spring.ecommerce.mongodb.repository;

import com.spring.ecommerce.mongodb.persistence.model.variants.VariantTypes;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VariantTypeRepository extends MongoRepository<VariantTypes, String> {
    List<VariantTypes> findByCategoryId(String categoryId);

    //List<VariantTypes> findByCategoryIds(List<String> categoryId);

}
