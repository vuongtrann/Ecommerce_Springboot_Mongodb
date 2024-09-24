package com.spring.ecommerce.mongodb.persistence.model.Auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Token {
    private String token;
    private boolean valid;
    private Date expires;
}
