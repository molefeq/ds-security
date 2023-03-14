package com.que.ads.ds.security.repository;

import com.que.ads.ds.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {

    @Query("""
                SELECT u from RefreshToken u WHERE u.refreshToken = :refreshToken
            """)
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
