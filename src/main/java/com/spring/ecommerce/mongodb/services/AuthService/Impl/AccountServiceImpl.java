package com.spring.ecommerce.mongodb.services.AuthService.Impl;

import com.spring.ecommerce.mongodb.persistence.Enum.Role;
import com.spring.ecommerce.mongodb.persistence.model.Auth.Account;
import com.spring.ecommerce.mongodb.persistence.model.Customer;
import com.spring.ecommerce.mongodb.repository.AuthRepository.AccountRepository;
import com.spring.ecommerce.mongodb.services.AuthService.AccountService;
import com.spring.ecommerce.mongodb.services.Impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountException;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerServiceImpl customerService;

    @Override
    public Account registration(Account account) {
        try{
            if (accountRepository.existsByEmail(account.getEmail())) {
                throw new AccountException("User already exists");
            }

            if (account.getEmail().isEmpty() || account.getPassword().isEmpty()) {
                throw new AccountException("Username or password cannot be empty");
            }
            else {

                account.setEmail(account.getEmail().trim());
                PasswordEncoder passwordEndcoder = new BCryptPasswordEncoder(10);
                account.setPassword(passwordEndcoder.encode(account.getPassword()).trim());



                Customer customer = new Customer(account);
//
//                customer.setEmail(account.getEmail());
//                customer.setFullName(account.getFullName());
//                customer.setPhone(account.getPhone());
//                customer.getRoles().add(Role.ROLE_BUYER.name());
//
                customerService.saveCustomer(customer);

                account.setCustomerId(customer.getId());
                return accountRepository.save(account);
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
            return accountRepository.findById(id);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        try{
            return accountRepository.findAll();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public boolean existsEmail(String email) {
        return accountRepository.existsByEmail(email);
    }
}
