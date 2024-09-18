package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.model.Customer;
import com.spring.ecommerce.mongodb.repository.CustomerRepository;
import com.spring.ecommerce.mongodb.services.CustomerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerServices {

    @Autowired
    CustomerRepository CustomerRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer getCustomerById(String id) {
        Customer customer = CustomerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return customer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        return List.of();
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(String id,Customer customer) {
        try {
            Customer oldCustomer = getCustomerById(id);
            if (customer.getName()!=null){
                oldCustomer.setName(customer.getName());
            }
            if (customer.getAddress()!=null){
                oldCustomer.setAddress(customer.getAddress());
            }
            if (customer.getPhone()!=null){
                oldCustomer.setPhone(customer.getPhone());
            }
            if (customer.getEmail()!=null){
                oldCustomer.setEmail(customer.getEmail());
            }
            return customerRepository.save(oldCustomer);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteCustomer(String id) {

    }
}
