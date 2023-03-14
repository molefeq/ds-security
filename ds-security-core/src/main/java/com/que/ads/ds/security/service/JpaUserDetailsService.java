package com.que.ads.ds.security.service;

import com.que.ads.ds.security.models.security.SecurityUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.login(username);

        UserDetails userDetails = user.map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username was not found %s", username)));

        return userDetails;
    }
}
