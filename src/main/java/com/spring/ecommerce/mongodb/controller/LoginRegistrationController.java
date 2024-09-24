package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Auth.Account;
import com.spring.ecommerce.mongodb.services.AuthService.Impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/")
public class LoginRegistrationController {
    @Autowired
    private AccountServiceImpl accountService;


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<Account> registerUser(@RequestBody() Account account){
        try{
            if (account.getEmail().isEmpty() || account.getPassword().isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(accountService.registration(account), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
