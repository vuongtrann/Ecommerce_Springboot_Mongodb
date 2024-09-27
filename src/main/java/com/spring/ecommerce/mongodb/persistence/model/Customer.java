package com.spring.ecommerce.mongodb.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring.ecommerce.mongodb.persistence.Enum.Role;
import com.spring.ecommerce.mongodb.persistence.model.Auth.Account;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Customer {
    @Id
    @Generated
    private String id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private List<String> roles = new ArrayList<>();
    private String token;


    public Customer(Account account) {
        this.fullName = account.getFullName();
        this.email = account.getEmail();
        this.phone = account.getPhone();
        this.roles.add(Role.ROLE_BUYER.name());
        this.token="";
    }

}




