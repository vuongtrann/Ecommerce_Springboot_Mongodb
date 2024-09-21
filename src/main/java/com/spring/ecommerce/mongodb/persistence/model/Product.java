package com.spring.ecommerce.mongodb.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.spring.ecommerce.mongodb.persistence.model.variants.ProductVariants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;

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

    private Double msrp;

    private Double salePrice;

    private Double price;

    private Double rating;

    private int quantity;

    private int noOfReviews;


    private String brandName;

    private String sellingTypes;

    @DocumentReference
    private List<Category> categories;

    @DocumentReference
    private ProductDimensions dimensions;

    private boolean hasVariants;

    @DocumentReference
    private List<ProductVariants> variants ;

    @Transient
    private boolean hasCollection;

    @DocumentReference
    private List<Collection> collections;

    @JsonIgnore
    private int viewCount;

    @JsonIgnore
    private int quantitySold;

    @JsonIgnore
    private int remainingQuantity;

    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;

    public Product(String name, String description, Double msrp, Double salePrice, Double price,
                   int quantity, String brandName, String sellingTypes, List<Category> items, ProductDimensions dimensions) {
        this.name = name;
        this.description = description;
        this.msrp = msrp;
        this.salePrice = salePrice;
        this.price = price;
        this.quantity = quantity;
        this.brandName = brandName;
        this.sellingTypes = sellingTypes;
        this.categories = items;
        this.dimensions = dimensions;
       // this.variants = variants;
    }
}
