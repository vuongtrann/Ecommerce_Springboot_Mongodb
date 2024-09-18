package com.spring.ecommerce.mongodb.persistence.model;

import lombok.Data;

@Data
public abstract class User {

    private String name;
    private String email;
    private String phone;
    private String address;


    public User(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
}
