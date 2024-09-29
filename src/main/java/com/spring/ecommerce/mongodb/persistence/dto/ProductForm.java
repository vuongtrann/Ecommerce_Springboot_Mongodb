package com.spring.ecommerce.mongodb.persistence.dto;

import com.spring.ecommerce.mongodb.persistence.model.Category;
import com.spring.ecommerce.mongodb.persistence.model.ProductDimensions;
import com.spring.ecommerce.mongodb.persistence.model.variants.ProductVariants;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductForm {

    private String name;

    private List<String> imageURL;

    private String primaryImageURL;

    private String description;

    private String SKU;
    private int quantityAvailable;
    private int soldQuantity;
    private double price;
    private double salePrice;
    private double MRSP;

    private int noOfReviews = 0;

    private String brandName;

    private String sellingTypes;
    private List<String> categories = new ArrayList<>();
    private ProductDimensions dimensions;
    private boolean hasVariants;
    private List<ProductVariants> variants = new ArrayList<>();
    private Map<String,List<String>> options;
    private List<String> collections;
    //private List<ProductSpecification> specifications = new ArrayList<>();
    private Map<String,String> specifications;
}
