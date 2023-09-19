package com.icsd.demo.repositories;

import com.icsd.demo.models.TokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenModel, Long> {
    Optional<TokenModel> findByCode(String code);
}
