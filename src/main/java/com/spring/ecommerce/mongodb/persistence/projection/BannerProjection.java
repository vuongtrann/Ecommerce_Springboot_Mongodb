package com.spring.ecommerce.mongodb.persistence.projection;

import java.time.LocalDate;

public interface BannerProjection {
    String getId();
    String getUrl();
    LocalDate getStartDate();
    LocalDate getEndDate();
}
