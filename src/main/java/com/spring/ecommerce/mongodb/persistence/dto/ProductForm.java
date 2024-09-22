package com.spring.ecommerce.mongodb.persistence.dto;

import com.spring.ecommerce.mongodb.persistence.model.Collection;
import com.spring.ecommerce.mongodb.persistence.model.ProductDimensions;
import com.spring.ecommerce.mongodb.persistence.model.variants.ProductVariants;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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

    private String primaryImageURL;

    private String description;

    private Double rating = 0.0;

    private int noOfReviews = 0;

    private String brandName;

    private String sellingTypes;
    private List<String> categories = new ArrayList<>();
    private ProductDimensions dimensions;
    private boolean hasVariants;
    private List<ProductVariants> variants = new ArrayList<>();
}
