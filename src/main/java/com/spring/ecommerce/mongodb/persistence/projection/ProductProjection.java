package com.spring.ecommerce.mongodb.persistence.projection;

import java.util.List;

public interface ProductProjection {
    String getId();
    String getName();
    List<String> getImageURL();
    String getPrimaryImageURL();
    String getDescription();
    double getRating();
    String getSKU();
    int getQuantityAvailable();
    int getSoldQuantity();
    double getPrice();
    double getSalePrice();
    double getMRSP();
    int getNoOfReviews();
    String getBrandName();
    String getSellingTypes();
    List<CategoryProjection> getCategories();
    int getViewCount();
    int getQuantitySold();



}
