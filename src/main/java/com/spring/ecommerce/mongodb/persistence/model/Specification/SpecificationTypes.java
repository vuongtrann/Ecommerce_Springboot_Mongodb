package com.spring.ecommerce.mongodb.persistence.model.Specification;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "specificationTypes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecificationTypes {
    @Id
    private String id;
    private String specificationType;
    private String categoryId;
}
