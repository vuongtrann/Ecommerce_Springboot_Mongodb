package com.spring.ecommerce.mongodb.services.AuthService;

import com.nimbusds.jose.JOSEException;
import com.spring.ecommerce.mongodb.persistence.model.Auth.Token;
import com.spring.ecommerce.mongodb.persistence.model.Customer;

import java.text.ParseException;

public interface AuthenticationService {
    public Customer login(String email, String password);
    public String generateToken(Customer userName);
    public Token verifyToken(Token token) throws JOSEException, ParseException;
}
