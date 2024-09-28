

package com.spring.ecommerce.mongodb.persistence.model.variants;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("variant_types")
public class VariantTypes {
    @Id
    private String id;
    private String type;
    private String categoryId;

    public VariantTypes(String type, String categoryId) {
        this.type = type;
        this.categoryId = categoryId;
    }

//    public VariantTypes(VariantTypes variantTypes, String categoryId) {
//    }

//    public VariantTypes(VariantTypes variantTypes, String categoryId) {
//    }
}
