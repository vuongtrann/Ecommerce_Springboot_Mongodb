package com.spring.ecommerce.mongodb.persistence.model.variants;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "option_values")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionValues {
    @Id
    private String id;
    private VariantType variantType;
    private String value;
    private String SKU;
    private int quantity;
    private double price;
    private double salePrice;
    private double MRSP;
}
