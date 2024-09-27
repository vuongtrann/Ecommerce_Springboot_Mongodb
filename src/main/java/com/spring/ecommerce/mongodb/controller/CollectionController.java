package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.Product;
import com.spring.ecommerce.mongodb.services.Impl.CollectionServicesImpl;
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

    private final CollectionServicesImpl collectionServices;

    /** Add Collections */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Category> addCollection(@RequestBody Category category) {
        try{
            return new ResponseEntity<>(collectionServices.addCollection(category), HttpStatus.OK);
        }catch (Exception e) {}
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /** Get All Collections */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCollections () {
        try{
            return new ResponseEntity<>(collectionServices.findAllCollections(), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    /** Add Product to Collection */
    @RequestMapping(value = "/{collectionId}/product/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<Product> addCollection(@PathVariable(value = "collectionId") String collectionId
            , @PathVariable(value = "productId") String productId){
        try {
            if (productId.isEmpty() || collectionId.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(collectionServices.addProduct(collectionId,productId),HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }





}
