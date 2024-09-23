package com.spring.ecommerce.mongodb.persistence.model.variants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "variant_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantOptions {
    @Id
//    @JsonIgnore
    private String id;
    private VariantType variantTypes;
    private String value;

    public VariantOptions(VariantType variantTypes, String value) {
        this.variantTypes = variantTypes;
        this.value = value;
    }
}
