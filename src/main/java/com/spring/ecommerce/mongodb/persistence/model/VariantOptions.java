package com.spring.ecommerce.mongodb.persistence.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "variant_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantOptions {
    @Id
    private String id;
    private VariantType variantTypes;
    private String value;
}
