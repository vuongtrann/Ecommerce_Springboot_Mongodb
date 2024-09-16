package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.dto.ProductForm;
import com.spring.ecommerce.mongodb.persistence.model.Product;
import com.spring.ecommerce.mongodb.services.ProductServices;
import com.spring.ecommerce.mongodb.services.S3Services;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ProductController {

    private final ProductServices productServices;
    private final S3Services s3Services;



    /**FIND ALL PRODUCT*/
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Product> findAllProduct() {
        return productServices.findAll();
    }

    /**Get product by id*/
    @RequestMapping(value = "/{productId}",method = RequestMethod.GET)
    public ResponseEntity<Product> getProductById(@PathVariable("productId") String productId) {
        if (!productServices.findById(productId).isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(productServices.findById(productId).get(), HttpStatus.OK);
        }
    }

    /**ADD NEW PRODUCT*/
    @RequestMapping(value = "",method = RequestMethod.POST)
    public Product addProduct(@RequestBody ProductForm product) {
        return productServices.addProduct(product);
    }

    /**Upload Image*/
    @PostMapping("/{productId}/upload/image")
    public ResponseEntity uploadImage(@PathVariable("productId") String productId, @RequestParam("file") MultipartFile[] files){
        List<File> fileList = new ArrayList<>();
        String keyUrl = "product/" + productId + "/" ;
        try {
            Product product = productServices.findById(productId).orElseThrow(()->new RuntimeException("Product with id : " + productId + " not found"));
            for (MultipartFile file : files) {
                File localFile = File.createTempFile("image_",file.getOriginalFilename());
                file.transferTo(localFile);
                fileList.add(localFile);
            }
            List<String> fileURLs = s3Services.upload(productId,fileList,keyUrl);
            List<String> oldFileURLs = product.getImageURL();
            if (oldFileURLs != null) {
                fileURLs.addAll(oldFileURLs);
                product.setImageURL(fileURLs);
                product.setPrimaryImageURL(fileURLs.get(0));
            }else {
                product.setImageURL(fileURLs);
                product.setPrimaryImageURL(fileURLs.get(0));
            }

            for (File file : fileList) {
                file.delete();
            }
            return new ResponseEntity<>(productServices.save(product), HttpStatus.CREATED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @RequestMapping(value = "/{productId}/delete/image",method = RequestMethod.DELETE)
    public ResponseEntity deleteImage(@PathVariable("productId") String productId, @RequestBody List<String> url) {
        if (productServices.findById(productId).isPresent()) {
            Product product = productServices.findById(productId).get();
            List<String> imageURLs = product.getImageURL();
            imageURLs.removeAll(url);
            s3Services.deleteImagesByUrls(url);
            product.setImageURL(imageURLs);

            return new ResponseEntity<>(productServices.save(product),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Product with id : " + productId + " not found", HttpStatus.NOT_FOUND);
        }

    }
}
