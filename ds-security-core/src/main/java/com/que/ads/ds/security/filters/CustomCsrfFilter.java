package com.que.ads.ds.security.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

public class CustomCsrfFilter  implements Filter {
    public static final String CSRF_COOKIE_NAME = "X-XSRF-TOKEN";

    @Override
    public  void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;

        CsrfToken csrf = (CsrfToken) httpServletRequest.getAttribute("_csrf");

        if (csrf != null) {
            Cookie cookie =  WebUtils.getCookie(httpServletRequest, CSRF_COOKIE_NAME);
            String token = csrf.getToken();

            if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                cookie = new Cookie(CSRF_COOKIE_NAME, token);
                cookie.setPath("/");
                ((HttpServletResponse)response).addCookie(cookie);
            }
        }

        filterChain.doFilter(request, response);
    }
}

