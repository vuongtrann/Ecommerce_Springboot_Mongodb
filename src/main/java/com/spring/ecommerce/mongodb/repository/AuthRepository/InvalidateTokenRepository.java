package com.spring.ecommerce.mongodb.repository.AuthRepository;

import com.spring.ecommerce.mongodb.persistence.model.Auth.InvalidateToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidateTokenRepository extends JpaRepository<InvalidateToken, String> {
}
