package com.spring.ecommerce.mongodb.persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "productDimensions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDimensions {
    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("weight")
    private double weight;
    @JsonProperty("length")
    private double length;
    @JsonProperty("width")
    private double width;
    @JsonProperty("height")
    private double height;
}

