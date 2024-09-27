package com.spring.ecommerce.mongodb.controller.AuthController;

import com.spring.ecommerce.mongodb.persistence.model.Auth.Account;
import com.spring.ecommerce.mongodb.persistence.model.Auth.Token;
import com.spring.ecommerce.mongodb.persistence.model.Customer;
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
    public ResponseEntity<Object> registerUser(@RequestBody() Account account){
        try{

            if (accountService.existsEmail(account.getEmail())){
                return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
            }
            if (account.getEmail().isEmpty() || account.getPassword().isEmpty()){
                return new ResponseEntity<>("Email or password is Empty", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(accountService.registration(account), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<Customer> login(@RequestBody Account account){
        try {
            if (account.getPassword().isEmpty() || account.getEmail().isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(authenticationService.login(account.getEmail(), account.getPassword()), HttpStatus.OK);
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
