package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Review;
import com.spring.ecommerce.mongodb.services.Impl.ReviewServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ReviewController {


   @Autowired
    private ReviewServicesImpl reviewService;

    /** Create new*/
   @RequestMapping(value = "/{productId}/review", method = RequestMethod.POST)
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
       try {
           return new ResponseEntity<>(reviewService.save(review), HttpStatus.CREATED);
       }catch (Exception e){
           e.printStackTrace();
           return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }

   }


}
