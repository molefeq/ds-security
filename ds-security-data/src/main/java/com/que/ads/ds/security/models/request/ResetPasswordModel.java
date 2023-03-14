package com.que.ads.ds.security.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordModel {
    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Username is required.")
    private String password;
}
