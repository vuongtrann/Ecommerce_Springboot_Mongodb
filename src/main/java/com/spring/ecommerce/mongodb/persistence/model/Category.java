package com.spring.ecommerce.mongodb.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantType;
import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)

public class Category {
    @Id
    @Generated
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
    private boolean isFeature;
    private boolean isCollection;
    private String icon;
    private Banners banner;
    private List<VariantType> variantTypes;
    private List<Product> featureProducts = new ArrayList<>();
    private int noOfViews;
    private int noOfSold;
    @DocumentReference
    private List<Category> subCategories = new ArrayList<>();
    @Transient
    private List<String> parentId = new ArrayList<>();

}
