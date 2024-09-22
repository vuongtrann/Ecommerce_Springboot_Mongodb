package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Review;
import com.spring.ecommerce.mongodb.services.Impl.ReviewServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReviewController {


   @Autowired
    private ReviewServicesImpl reviewService;

    /** Create new*/
   @RequestMapping(value = "/products/review", method = RequestMethod.POST)
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
       try {
           return new ResponseEntity<>(reviewService.save(review), HttpStatus.CREATED);
       }catch (Exception e){
           e.printStackTrace();
           return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
       }

   }


   /** Find all */
    @RequestMapping(value = "/review/all", method = RequestMethod.GET)
    public ResponseEntity<List<Review>> getAllReviews() {
        try{
            return new ResponseEntity<>(reviewService.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /** Find by Id*/
    @RequestMapping(value = "/products/review/{reviewId}", method = RequestMethod.GET)
    public ResponseEntity<Review> getReviewById(@PathVariable String reviewId) {
        try{
            return new ResponseEntity<Review>(reviewService.findById(reviewId), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /** Find by productId*/
    @RequestMapping(value = "/review/product/{productId}", method = RequestMethod.GET)
    public ResponseEntity<List<Review>> getReviewByProductId(
            @PathVariable String productId,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {
        try{
            if (productId== null || productId.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(reviewService.findByProductId(productId, limit), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    /** Update */
    @RequestMapping(value = "/review/update",method = RequestMethod.PUT)
    public ResponseEntity<Review> update (@RequestBody Review review){
        try{
            if (review.getId() == null || review.getId().isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(reviewService.update(review), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    /** Detele */
    @RequestMapping(value = "review/{reviewId}/delete", method = RequestMethod.DELETE)
    public ResponseEntity<String> delete (@PathVariable String reviewId){
        try {
            if (reviewId == null || reviewId.isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            reviewService.deleteById(reviewId);
            return new ResponseEntity<>( "Deleted",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
