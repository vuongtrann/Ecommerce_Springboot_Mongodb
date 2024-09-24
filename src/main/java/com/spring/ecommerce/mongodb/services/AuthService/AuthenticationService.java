package com.spring.ecommerce.mongodb.services.AuthService;

import com.nimbusds.jose.JOSEException;
import com.spring.ecommerce.mongodb.persistence.model.Auth.Token;

import java.text.ParseException;

public interface AuthenticationService {
    public String authenticate(String userName, String password);
    public String generateToken(String userName);
    public Token verifyToken(Token token) throws JOSEException, ParseException;
}
