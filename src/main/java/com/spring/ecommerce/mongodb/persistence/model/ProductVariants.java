package com.spring.ecommerce.mongodb.persistence.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariants {

    private String id;
    private String productId;
    private List<VariantOptions> variantOptions;
}
