package com.spring.ecommerce.mongodb.persistence.dto;


import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
    private List<VariantDetails> variantDetails = new ArrayList<>() ;
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
    @AllArgsConstructor
    @Builder
    public static class VariantDetails {
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

