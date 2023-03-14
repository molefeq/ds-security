package com.que.ads.ds.security.config;

import com.que.ads.ds.security.filters.CustomCsrfFilter;
import com.que.ads.ds.security.filters.JWTAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${que.ads.security.csrf.exclude.path}")
    private String[] excludeCsrfPatterns;

    @Value("${que.ads.security.authentication.exclude.path}")
    private String[] anonymousPatterns;

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

       httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return httpSecurity
                .cors()
                .and()
                .csrf().ignoringRequestMatchers(excludeCsrfPatterns)
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests().requestMatchers(anonymousPatterns).permitAll()
                .and()
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
//                .rememberMe((remember) -> remember
//                        .rememberMeServices(rememberMeServices)
//                )
                .addFilterBefore(new CustomCsrfFilter(), CsrfFilter.class) // Csrf filter in which we will add the cookie
                .build();
    }

//    @Bean
//    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
//        TokenBasedRememberMeServices.RememberMeTokenAlgorithm encodingAlgorithm = TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256;
//        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("rem-me-key", userDetailsService, encodingAlgorithm);
//        rememberMe.setMatchingAlgorithm(TokenBasedRememberMeServices.RememberMeTokenAlgorithm.MD5);
//        return rememberMe;
//    }

    private CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = new CookieCsrfTokenRepository();


        repository.setCookieHttpOnly(false);
       // repository.setHeaderName(CustomCsrfFilter.CSRF_COOKIE_NAME);

        return repository;
    }
}
