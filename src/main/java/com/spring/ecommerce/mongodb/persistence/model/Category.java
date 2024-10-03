package com.spring.ecommerce.mongodb.persistence.model;

import com.spring.ecommerce.mongodb.persistence.model.Specification.SpecificationTypes;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantTypes;
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
//@JsonIgnoreProperties(ignoreUnknown = true)

public class Category {
    @Id
    @Generated
    private String id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private Boolean isFeature;
    private boolean isCollection;
    private String icon;
    private Banners banner;
    //private List<VariantType> variantType;
    //private List<Product> featureProducts = new ArrayList<>();
    private int noOfViews;
    private int noOfSold;
    @DocumentReference
    private List<Category> subCategories = new ArrayList<>();
    @Transient
    private List<String> parentId = new ArrayList<>();

    @DocumentReference
    private List<SpecificationTypes> specificationTypes;

    @DocumentReference
    private List<VariantTypes> variantTypes;



}
