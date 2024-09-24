package com.spring.ecommerce.mongodb.persistence.model.Auth;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="account")
public class Account {
    @Id
    @GeneratedValue
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String phone;
    private boolean isConfirmed;
    private String customerId;
}
