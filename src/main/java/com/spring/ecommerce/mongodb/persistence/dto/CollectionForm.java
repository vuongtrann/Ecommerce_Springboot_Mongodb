package com.spring.ecommerce.mongodb.persistence.dto;

import com.spring.ecommerce.mongodb.persistence.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class CollectionForm {
    private String id;
    private String name;
    private int total;
    private List<Product> productDetails;
}
