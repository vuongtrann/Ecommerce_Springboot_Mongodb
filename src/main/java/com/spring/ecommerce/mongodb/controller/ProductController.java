package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.dto.ProductForm;
import com.spring.ecommerce.mongodb.persistence.model.Product;
import com.spring.ecommerce.mongodb.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {
    @Autowired
    private ProductServices productServices;

    public ProductController(ProductServices productServices) {
        this.productServices = productServices;
    }


    @GetMapping("")
    public List<Product> getAllProducts() {
        return productServices.findAll();
    }

    @PostMapping("")
    public Product createProduct(@RequestBody ProductForm product) {
        return productServices.addProduct(product);
    }
}
