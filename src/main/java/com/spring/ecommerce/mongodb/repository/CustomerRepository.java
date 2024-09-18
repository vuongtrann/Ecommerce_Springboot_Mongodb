package com.spring.ecommerce.mongodb.repository;

import com.spring.ecommerce.mongodb.persistence.model.Customer;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    @Aggregation(pipeline = {
            "{$match:  {_id:  ?0}}"
    })
    public Optional<Customer> findById(String id);

    @Aggregation(pipeline = {
            "{$match: {}}"
    })
    public List<Customer> findAll();


}
