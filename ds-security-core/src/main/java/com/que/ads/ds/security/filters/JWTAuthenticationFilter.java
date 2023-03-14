package com.que.ads.ds.security.filters;

import com.que.ads.ds.security.jwt.JwtTokenManager;
import com.que.ads.ds.security.models.security.AuthenticationDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenManager tokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken == null ||
                jwtToken.length() == 0) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!tokenManager.validate(jwtToken)) {
            throw new RuntimeException("Token has expired.");
        }

        final String username = tokenManager.getUsernameFromToken(jwtToken);

        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }

        List<String> authorities = tokenManager.getAuthorities(jwtToken);

        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities.stream().map(SimpleGrantedAuthority::new).toList()
        );

        auth.setDetails(AuthenticationDetails.builder()
                .authorities(authorities)
                .userId(Integer.parseInt(tokenManager.getClaim(jwtToken, "userId")))
                .build());

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }
}
