package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.model.Customer;

import java.util.List;

public interface CustomerServices {
    public Customer getCustomerById(String id);
    public List<Customer> getAllCustomers();
    public Customer saveCustomer(Customer customer);
    public Customer updateCustomer(String id,Customer customer);
    public void deleteCustomer(String id);

}
