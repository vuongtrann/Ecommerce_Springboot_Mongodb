package com.spring.ecommerce.mongodb.persistence.projection;

import java.util.List;

public interface ProductProjection {
    String getId();
    String getName();
    List<String> getImageURL();
    String getPrimaryImageURL();
    String getDescription();
    double getRating();
    int getNoOfReviews();
    String getBrandName();
    String getSellingTypes();
    List<CategoryProjection> getCategories();


}
