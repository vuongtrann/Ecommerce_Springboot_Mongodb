package com.spring.ecommerce.mongodb.services.Impl;

import com.spring.ecommerce.mongodb.persistence.model.Account;
import com.spring.ecommerce.mongodb.repository.LoginRegistrationRepository;
import com.spring.ecommerce.mongodb.services.LoginRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import java.util.List;
import java.util.Optional;

@Service
public class LoginRegistrationServiceImpl implements LoginRegistrationService {
    @Autowired
    private LoginRegistrationRepository loginRegistrationRepository;

    @Override
    public Account registration(Account account) {
        try{
            if (account.getUserName().isEmpty() || account.getPassword().isEmpty()) {
                throw new AccountException("Username or password cannot be empty");
            }
            else {
                account.setUserName(account.getUserName().trim());
                account.setPassword(account.getPassword().trim());
                return loginRegistrationRepository.save(account);
            }
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Optional<Account> updateAccount(Account account) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> deleteAccount(Account account) {
        return Optional.empty();
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        try{
            if(id ==null){
                throw new RuntimeException("Id cannot be empty");
            }
            return loginRegistrationRepository.findById(id);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        try{
            return loginRegistrationRepository.findAll();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
