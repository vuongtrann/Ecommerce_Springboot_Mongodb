package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Customer;
import com.spring.ecommerce.mongodb.repository.CustomerRepository;
import com.spring.ecommerce.mongodb.services.Impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/v1/customer")
public class CustomerController {
    @Autowired
    private CustomerServiceImpl customerService;


    /** Add Customer */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
        try{
            return new ResponseEntity<>(customerService.saveCustomer(customer), HttpStatus.CREATED);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /** Get By Id*/
    @RequestMapping(value = "/{customerid}", method = RequestMethod.GET)
    public ResponseEntity<Customer> getCustomer(@PathVariable("customerid") String customerid) {
        if (customerid == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try{
            return new ResponseEntity<>(customerService.getCustomerById(customerid), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /** Update Customer */
    @RequestMapping(value = "/{customerId}/update", method = RequestMethod.PUT)
    public ResponseEntity<Customer> updateCustomer(@PathVariable("customerId") String customerId, @RequestBody Customer customer) {
        if (customerId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(customerService.updateCustomer(customerId, customer), HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

    }


    /** Get all */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Customer>> getAllCustomers() {
        try{
            return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /** Delete */
    @RequestMapping(value = "/{customerId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerId") String customerId) {
        if (customerId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
