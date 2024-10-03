package com.spring.ecommerce.mongodb.services.AuthService.Impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.spring.ecommerce.mongodb.persistence.model.Auth.Account;
import com.spring.ecommerce.mongodb.persistence.model.Auth.InvalidateToken;
import com.spring.ecommerce.mongodb.persistence.model.Auth.Token;
import com.spring.ecommerce.mongodb.persistence.model.Customer;
import com.spring.ecommerce.mongodb.repository.AuthRepository.AccountRepository;
import com.spring.ecommerce.mongodb.repository.AuthRepository.InvalidateTokenRepository;
import com.spring.ecommerce.mongodb.services.AuthService.AuthenticationService;
import com.spring.ecommerce.mongodb.services.Impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${auth.SIGNER_KEY}")
    private String  SIGNER_KEY;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InvalidateTokenRepository invalidateTokenRepository;

    @Autowired
    private CustomerServiceImpl customerService;


    @Override
    public Customer login (String email, String password) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException ("Email not found"));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        if (passwordEncoder.matches(password, account.getPassword())) {
            Customer customer = customerService.getCustomerById(account.getCustomerId());
            customer.setToken( generateToken(customer));

            return customerService.saveCustomer(customer);
        }
        throw new RuntimeException( "Invalid account or password");
    }

    @Override
    public void logout(Token token ) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token.getToken());
        Date expiration = signedJWT.getJWTClaimsSet().getExpirationTime();

        if ( expiration.before(new Date()) && signedJWT.verify(verifier)   ) {
            throw new RuntimeException( "Expired token");
        }

        InvalidateToken invalidateToken  = new InvalidateToken();
        invalidateToken.setId(signedJWT.getJWTClaimsSet().getJWTID());
        invalidateToken.setExpiryDate(expiration);

        invalidateTokenRepository.save(invalidateToken);


    }




    @Override
    public String generateToken(Customer customer){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);


        JWTClaimsSet  jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(customer.getEmail())
                .issuer(customer.getFullName())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope" ,buildScope(customer))
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

        if (invalidateTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            token.setValid(false);
            token.setExpires(null);
            token.setToken(null);
            return token;
        }

        token.setValid(signedJWT.verify(verifier)   && expirationDate.after(new Date()));
        token.setExpires(expirationDate);
        return token;


    }



    private String buildScope(Customer customer) {
        StringJoiner scope = new StringJoiner(" ");
        if (!customer.getRoles().isEmpty()) {
            customer.getRoles().forEach(role -> scope.add(role));
            return scope.toString();
        }
        return "";
    }


}
