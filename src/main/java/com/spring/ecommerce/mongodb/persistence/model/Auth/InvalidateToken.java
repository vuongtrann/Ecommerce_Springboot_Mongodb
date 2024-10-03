package com.spring.ecommerce.mongodb.persistence.model.Auth;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invalidateToken")
@Entity
public class InvalidateToken {
    @Id
    private String id;
    private Date expiryDate;
}
