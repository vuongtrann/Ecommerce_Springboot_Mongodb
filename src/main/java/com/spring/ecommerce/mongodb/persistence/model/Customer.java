package com.spring.ecommerce.mongodb.persistence.model;

import com.spring.ecommerce.mongodb.persistence.Enum.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.spring.ecommerce.mongodb.persistence.Enum.Role.ROLE_VENDOR;

@Document("customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends User{
    @Id
    @Generated
    private String id;
    private String role = Role.ROLE_CUSTOMER.name();


}