package com.que.ads.ds.security.models.security;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuthenticationDetails {
    private List<String> authorities;
    private int userId;
}
