package com.spring.ecommerce.mongodb.repository.AuthRepository;

import com.spring.ecommerce.mongodb.persistence.model.Auth.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String eamil);

    public boolean existsByEmail(String email);

}
