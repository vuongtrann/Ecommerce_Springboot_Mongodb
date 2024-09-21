package com.spring.ecommerce.mongodb.persistence.model.variants;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariants {

    @Id
    private String id;
    private double rating;
    //private String productId;
    private String SKU;
    private int quantityAvailable;
    private int soldQuantity;
    private double price;
    private double salePrice;
    private double MRSP;
    private List<String> imageURLs;
    private List<VariantOptions> variantOptions;

}
