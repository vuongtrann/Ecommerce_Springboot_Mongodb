package com.spring.ecommerce.mongodb.persistence.dto;

import com.spring.ecommerce.mongodb.persistence.model.ProductDimensions;
import com.spring.ecommerce.mongodb.persistence.model.variants.ProductVariants;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductForm {
    private String name;
    private List<String> imageURL;
    private String description;
    private Double msrp;
    private Double salePrice;
    private Double price;
    private int quantity;
    private String SKU;
    private String brandName;
    private boolean hasVariants;
    private String sellingTypes;
    private List<String> categories = new ArrayList<>();
    private List<ProductVariants> variants = new ArrayList<>();
    private ProductDimensions dimensions;
}
