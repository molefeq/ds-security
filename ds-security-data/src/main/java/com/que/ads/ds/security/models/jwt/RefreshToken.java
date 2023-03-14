package com.que.ads.ds.security.models.jwt;

import com.que.ads.ds.security.models.security.SecurityUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    private long id;
    private SecurityUser user;
    private String token;
    private Instant expiryDate;
}
