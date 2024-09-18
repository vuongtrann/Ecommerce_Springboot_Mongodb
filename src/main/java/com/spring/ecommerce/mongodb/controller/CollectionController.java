package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Collection;
import com.spring.ecommerce.mongodb.services.CollectionServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/collections")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CollectionController {
    private final CollectionServices collectionServices;

    @RequestMapping(value = "",method = RequestMethod.GET)
    public List<Collection> getCollections() {
        return collectionServices.findAllCollections();
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseEntity addCollection(@RequestBody Collection collection) {
        return new ResponseEntity<>(collectionServices.addCollection(collection), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{collectionId}",method = RequestMethod.GET)
    public ResponseEntity getCollection(@PathVariable String collectionId) {
        return new ResponseEntity<>(collectionServices.findById(collectionId), HttpStatus.OK);
    }
}
