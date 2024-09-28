package com.spring.ecommerce.mongodb.persistence.model.variants;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "variant_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantOptions {
    @Id
    private String id;
    private String variantTypes;
    private String value;

    public VariantOptions(String variantTypes, String value) {
        this.variantTypes = variantTypes;
        this.value = value;
    }
}
