package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.Collection;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import com.spring.ecommerce.mongodb.services.CollectionServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/collections")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CollectionController {

    private final CategoryServices categoryServices;

    /** Add Collections */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Category> addCategory(@RequestParam String name) {
        try{
            return new ResponseEntity<>(categoryServices.addCollection(name), HttpStatus.OK);
        }catch (Exception e) {}
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /** Get All Collections */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCollections () {
        try{
            return new ResponseEntity<>(categoryServices.getAllCollections(), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
