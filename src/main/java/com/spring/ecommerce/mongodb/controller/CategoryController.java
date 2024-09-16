package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.Product;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryServices categoryServices;

    public CategoryController(CategoryServices categoryServices) {
        this.categoryServices = categoryServices;
    }

    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategory() {

        return new ResponseEntity<>(categoryServices.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryServices.save(category), HttpStatus.CREATED);
    }



    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        categoryServices.delete(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }








}
