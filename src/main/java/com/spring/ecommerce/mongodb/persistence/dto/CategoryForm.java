package com.spring.ecommerce.mongodb.persistence.dto;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryForm {
    @Id
    private String id;
    private String name;
    private int level;

    List<Long> categories = new ArrayList<>();

}
