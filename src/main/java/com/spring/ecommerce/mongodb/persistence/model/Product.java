package com.spring.ecommerce.mongodb.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.ecommerce.mongodb.persistence.model.variants.ProductVariants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Document(collection = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    private String id;

    private String name;

    private List<String> imageURL;

    private String primaryImageURL;

    private String description;

    private Double rating = 0.0;


    private String SKU;
    private int quantityAvailable;
    private int soldQuantity;
    private double price;
    private double salePrice;
    private double MRSP;


    private int noOfReviews = 0;

    private String brandName;

    private String sellingTypes;


    @DocumentReference
    private List<Category> categories;

    @DocumentReference
    private List<Category> collections;

    @DocumentReference
    private ProductDimensions dimensions;

    private boolean hasVariants;

    @DocumentReference
    private List<ProductVariants> variants ;

    private Map<String, List<String>> options;

    private Map<String,String> specifications;

    @JsonIgnore
    private int viewCount;

    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;

    public Product(String name, String description, String brandName, String sellingTypes, List<Category> items, ProductDimensions dimensions) {
        this.name = name;
        this.description = description;
        this.brandName = brandName;
        this.sellingTypes = sellingTypes;
        this.categories = items;
        this.dimensions = dimensions;
       // this.variants = variants;
    }
}
