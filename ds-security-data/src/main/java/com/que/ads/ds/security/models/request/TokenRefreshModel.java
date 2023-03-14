package com.que.ads.ds.security.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshModel {
    @NotBlank(message = "Refresh Token is required.")
    private String refreshToken;
}
