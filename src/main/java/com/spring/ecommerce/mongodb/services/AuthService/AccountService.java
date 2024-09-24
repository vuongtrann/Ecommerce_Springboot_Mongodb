package com.spring.ecommerce.mongodb.services.AuthService;

import com.spring.ecommerce.mongodb.persistence.model.Auth.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    public Account registration(Account account);
    public Optional<Account> updateAccount(Account account);
    public Optional<Account> deleteAccount(Account account);
    public Optional<Account> getAccountById(Long id);
    public List<Account> getAllAccounts();

    public boolean existsEmail(String email);
}
