package com.spring.ecommerce.mongodb.services;

import com.spring.ecommerce.mongodb.persistence.model.Account;

import java.util.List;
import java.util.Optional;

public interface LoginRegistrationService {
    public Account registration(Account account);
    public Optional<Account> updateAccount(Account account);
    public Optional<Account> deleteAccount(Account account);
    public Optional<Account> getAccountById(Long id);
    public List<Account> getAllAccounts();

}
