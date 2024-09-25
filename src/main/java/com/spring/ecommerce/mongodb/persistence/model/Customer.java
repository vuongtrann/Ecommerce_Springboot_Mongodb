package com.spring.ecommerce.mongodb.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.ecommerce.mongodb.persistence.Enum.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer extends User{
    @Id
    @Generated
    private String id;

    public Customer(String fullName, String email, String phone, String address) {
        super(fullName, email, phone, address);
        super.getRoles().add(Role.ROLE_BUYER.name());

    }
}




