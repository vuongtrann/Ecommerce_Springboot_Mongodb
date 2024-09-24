package com.spring.ecommerce.mongodb.services.AuthService.Impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.spring.ecommerce.mongodb.persistence.model.Auth.Account;
import com.spring.ecommerce.mongodb.persistence.model.Auth.Token;
import com.spring.ecommerce.mongodb.repository.AuthRepository.AccountRepository;
import com.spring.ecommerce.mongodb.services.AuthService.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${auth.SIGNER_KEY}")
    private String  SIGNER_KEY;

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public String authenticate(String email, String password) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException ("Email not found"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (passwordEncoder.matches(password, account.getPassword())) {
            String token = generateToken(email);
            return token;
        }
        return "Invalid account or password";
    }

    @Override
    public String generateToken(String email){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);


        JWTClaimsSet  jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer(email)
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .build();

        Payload   payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public Token verifyToken(Token token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token.getToken());
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();

        token.setValid(signedJWT.verify(verifier)   && expirationDate.after(new Date()));
        token.setExpires(expirationDate);

        return token;


    }




}