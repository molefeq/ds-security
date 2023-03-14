package com.que.ads.ds.security.models.request;

import jakarta.validation.constraints.Email;
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
public class RegisterModel {
    @NotBlank(message = "Name is required.")
    @Size(max = 100, message = "Name must not be more than 100 characters.")
    private String name;

    @NotBlank(message = "Lastname is required.")
    @Size(max = 100, message = "Lastname must not be more than 100 characters.")
    private String lastName;

    @NotBlank(message = "Email Address is required.")
    @Email(message = "Email Address is invalid.")
    private String emailAddress;

    @NotBlank(message = "Username is required.")
    @Size(min = 5, max = 200, message = "Username must be at least 5 characters.")
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 5, max = 20, message = "Password must be at least 5 characters but not more than 20 characters.")
    private String password;

    private String forgotPasswordKey;

    @NotBlank(message = "Role is required.")
    private int roleId;
}
