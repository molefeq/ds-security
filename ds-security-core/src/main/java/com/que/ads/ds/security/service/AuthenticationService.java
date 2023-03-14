package com.que.ads.ds.security.service;

import com.que.ads.ds.security.jwt.JwtTokenManager;
import com.que.ads.ds.security.models.request.LoginModel;
import com.que.ads.ds.security.models.request.TokenRefreshModel;
import com.que.ads.ds.security.models.response.AuthenticationResponseModel;
import com.que.ads.ds.security.models.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager manager;
    private final RefreshTokenService refreshTokenService;
    private final JwtTokenManager jwtTokenManager;
    private final UserService userService;

    public AuthenticationResponseModel authenticate(LoginModel loginModel) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword())
        );

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("User not authenticated.");
        }

        var userDetails = (SecurityUser) authentication.getDetails();
        var token = jwtTokenManager.generate(userDetails);
        var refreshToken = refreshTokenService.createRefreshToken(userDetails);

        userService.login(loginModel.getUsername());

        AuthenticationResponseModel authenticationModel = new AuthenticationResponseModel()
                .accessToken(token)
                .refreshToken(refreshToken.getRefreshToken());

        return authenticationModel;
    }

    public AuthenticationResponseModel refreshToken(TokenRefreshModel tokenRefreshModel) {
        var refreshToken = refreshTokenService.getRefreshToken(tokenRefreshModel.getRefreshToken());

        refreshTokenService.verifyExpiration(refreshToken);

        var userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getDetails();
        var token = jwtTokenManager.generate(userDetails);

        refreshTokenService.createRefreshToken(userDetails, refreshToken);

        AuthenticationResponseModel authenticationModel = new AuthenticationResponseModel()
                .accessToken(token)
                .refreshToken(refreshToken.getRefreshToken());

        return authenticationModel;
    }
}
