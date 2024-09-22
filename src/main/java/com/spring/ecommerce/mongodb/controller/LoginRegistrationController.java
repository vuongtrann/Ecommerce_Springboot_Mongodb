package com.spring.ecommerce.mongodb.controller;

import com.spring.ecommerce.mongodb.persistence.model.Account;
import com.spring.ecommerce.mongodb.services.Impl.LoginRegistrationServiceImpl;
import com.spring.ecommerce.mongodb.services.LoginRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class LoginRegistrationController {
    @Autowired
    private LoginRegistrationServiceImpl loginRegistrationService;


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<Account> registerUser(@RequestBody() Account account){
        try{
            if (account.getUserName().isEmpty() || account.getPassword().isEmpty()){
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(loginRegistrationService.registration(account), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
