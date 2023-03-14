package com.que.ads.ds.security.models.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginModel {

    @NotBlank(message = "Username is required.")
    @Size(min = 4, max = 200, message = "Username must be at least 4 characters.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 5, max = 20, message = "Password must be at least 5 characters but not more than 20 characters.")
    private String password;
}
