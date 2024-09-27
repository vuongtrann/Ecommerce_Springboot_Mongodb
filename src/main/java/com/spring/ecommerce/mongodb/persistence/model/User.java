package com.spring.ecommerce.mongodb.persistence.model;

import com.spring.ecommerce.mongodb.persistence.Enum.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public abstract class User {

    private String fullName;
    private String email;
    private String phone;
    private String address;
    private List<String> roles;
    private String token;


    public User(String fullName, String email, String phone, String address) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.token ="";
    }
}
