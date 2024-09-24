package com.spring.ecommerce.mongodb.controller.AuthController;

import com.spring.ecommerce.mongodb.persistence.model.Auth.Account;
import com.spring.ecommerce.mongodb.persistence.model.Auth.Token;
import com.spring.ecommerce.mongodb.services.AuthService.Impl.AccountServiceImpl;
import com.spring.ecommerce.mongodb.services.AuthService.Impl.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthenticationController {
    @Autowired
    private AccountServiceImpl accountService;

    @Autowired
    private AuthenticationServiceImpl authenticationService;


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<Account> registerUser(@RequestBody() Account account){
        try{

            if (accountService.existsEmail(account.getEmail())){
                return new ResponseEntity<>(null, HttpStatus.OK);
            }
            if (account.getEmail().isEmpty() || account.getPassword().isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            PasswordEncoder passwordEndcoder = new BCryptPasswordEncoder(10);
            account.setPassword(passwordEndcoder.encode(account.getPassword()));
            return new ResponseEntity<>(accountService.registration(account), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<String> login(@RequestBody Account account){
        try {
            if (account.getPassword().isEmpty() || account.getEmail().isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(authenticationService.authenticate(account.getEmail(), account.getPassword()), HttpStatus.OK);
        }
        catch (Exception e){
           e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @RequestMapping(value = "/exists", method = RequestMethod.GET)
    public ResponseEntity<Boolean> existsUser(@RequestParam String email){
        try{
            return new ResponseEntity<>(accountService.existsEmail(email), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<Token> verifyToken(@RequestBody Token token){
        try {
            return new ResponseEntity<>(authenticationService.verifyToken(token), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
