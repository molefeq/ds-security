package com.que.ads.ds.security.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseModel {
    private String accessToken;
    private String refreshToken;

    public AuthenticationResponseModel accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public AuthenticationResponseModel refreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }
}
