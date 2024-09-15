package com.spring.ecommerce.mongodb.persistence.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document("banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Banners {
    @Id
    @Generated
    private String id;
    private String url;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive = true;
    private String categoryId;

}


