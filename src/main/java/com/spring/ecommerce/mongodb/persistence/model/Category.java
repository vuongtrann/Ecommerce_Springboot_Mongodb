package com.spring.ecommerce.mongodb.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.boot.Banner;
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
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty(value = "level", defaultValue = "1")
    private int level = 1;
    @JsonProperty("createAt")
    private LocalDateTime createdAt;
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    private boolean isActive;
    private boolean isFeature;
    private String icon;
    private Banners banner;
    private boolean isCollection;

    private List<Product> featureProducts = new ArrayList<>();


    //update
    private List<Category> parents = new ArrayList<>() ;
    @Transient
    private List<String> parentIds;
   private int noOfViews;
   private int noOfSold;



    public Category(String name, int level, List<Category> subCategory) {
        this.name = name;
        this.level = level;
    }
}
