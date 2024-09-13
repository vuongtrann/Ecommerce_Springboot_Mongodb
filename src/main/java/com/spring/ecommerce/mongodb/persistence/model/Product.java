package com.spring.ecommerce.mongodb.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("imageURL")
    private List<String> imageURL;

    @JsonProperty("primaryImageURL")
    private String primaryImageURL;

    @JsonProperty("description")
    private String description;

    @JsonProperty("msrp")
    private Double msrp;

    @JsonProperty("salePrice")
    private Double salePrice;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("rating")
    private Double rating;

    @JsonProperty("viewCount")
    @JsonIgnore
    private int viewCount;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("SKU")
    private String SKU;

    @JsonProperty("quantitySold")
    @JsonIgnore
    private int quantitySold;

    @JsonProperty("remainingQuantity")
    @JsonIgnore
    private int remainingQuantity;

    @JsonProperty("brandName")
    private String brandName;

    @JsonProperty("sellingTypes")
    private String sellingTypes;


    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;



    @DocumentReference
    private List<Category> categories;

    @DocumentReference
    private ProductDimensions dimensions;


    public Product(String name, String description, Double msrp, Double salePrice, Double price, int quantity, String sku, String sellingTypes, List<Category> items, ProductDimensions dimensions) {
        this.name = name;
        this.description = description;
        this.msrp = msrp;
        this.salePrice = salePrice;
        this.price = price;
        this.quantity = quantity;
        this.brandName = sku;
        this.sellingTypes = sellingTypes;
        this.categories = items;
        this.dimensions = dimensions;
    }
}
