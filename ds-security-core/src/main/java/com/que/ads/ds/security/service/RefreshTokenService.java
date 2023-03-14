package com.que.ads.ds.security.service;

import com.que.ads.ds.common.utils.SqlTimestampUtil;
import com.que.ads.ds.security.entity.RefreshToken;
import com.que.ads.ds.security.models.security.SecurityUser;
import com.que.ads.ds.security.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final long REFRESH_TOKEN_VALIDITY = (5 * 60) + 10;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken getRefreshToken(String token) {
        var refreshToken = refreshTokenRepository.findByRefreshToken(token);

        return refreshToken.get();
    }

    public RefreshToken createRefreshToken(SecurityUser userDetails) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setExpiryDate(SqlTimestampUtil.addMilliSeconds(REFRESH_TOKEN_VALIDITY * 1000));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public void createRefreshToken(SecurityUser userDetails, RefreshToken refreshToken) {
        refreshToken.setExpiryDate(SqlTimestampUtil.addMilliSeconds(REFRESH_TOKEN_VALIDITY * 1000));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());

        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(SqlTimestampUtil.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token was expired. Please make a new sign in request");
        }

        return refreshToken;
    }
}

