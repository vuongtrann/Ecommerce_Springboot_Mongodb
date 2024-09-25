package com.spring.ecommerce.mongodb.persistence.dto;

import com.spring.ecommerce.mongodb.persistence.model.Review;
import com.spring.ecommerce.mongodb.persistence.model.variants.VariantType;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewForm {
    @Id
    private String id;
    private String title;
    private String description;
    private int rating;
    private LocalDateTime createdAt;

    private ProductDetail productsDetail;
    private VariantDetail variantDetails;
    private CustomerDetail customerDetails;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductDetail {
        private String id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VariantDetail {
        private String variantTypes;
        private String value;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CustomerDetail {
        private String id;
        private String fullName;
    }
}

