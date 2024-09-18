package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.services.CategoryServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/category")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CategoryController {

    private final CategoryServices categoryServices;

    /**Get categories*/
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getAllCategory() {

        return new ResponseEntity<>(categoryServices.findAll(), HttpStatus.OK);
    }
    /**add categories*/
    @RequestMapping(value = "",method = RequestMethod.POST)
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryServices.save(category), HttpStatus.CREATED);
    }

    /**Get category by id*/
    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    public ResponseEntity<Category> getCategoryById(@PathVariable String categoryId) {
        try {
            Optional<Category> category = categoryServices.findById(categoryId);
            if (category.isPresent()){
                return new ResponseEntity<>(category.get(), HttpStatus.OK);
            }
            else   return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        categoryServices.delete(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }


    @RequestMapping(value = "/top", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getTopCategory(@RequestParam(value ="limit", defaultValue = "10") int limit) {
        try{
            return new ResponseEntity<>(categoryServices.getTopCategory(limit), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
