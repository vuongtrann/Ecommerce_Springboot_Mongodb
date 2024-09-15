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
    @JsonProperty("level")
    private int level;
    @JsonProperty("createAt")
    private LocalDateTime createdAt;
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;

    private boolean isActive;
    private boolean isFeature;
    private String icon;
    private Banners banner;

    private List<Product> featureProducts = new ArrayList<>();

    @DocumentReference
    private List<Category> categories ;

    @DocumentReference
    private List<Category> subCategory ;



    @Transient
    private int noOfViews;

    @Transient
    private int productsSold;



    public Category(String name, int level, List<Category> categories) {
        this.name = name;
        this.categories = categories;
        this.level = level;
    }
}
