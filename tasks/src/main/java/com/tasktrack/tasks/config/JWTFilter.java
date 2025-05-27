package com.tasktrack.tasks.config;

import com.tasktrack.tasks.domain.oauth2User.dto.CustomOAuth2User;
import com.tasktrack.tasks.domain.oauth2User.entity.UserRole;
import com.tasktrack.tasks.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = null;

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie: cookies) {
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

        if (authorization == null || jwtUtil.isExpired(authorization)) {
            System.out.println("token null");

            filterChain.doFilter(request, response);
            // no need to execute (invalid jwt)
            return;
        }

        String token = authorization;

        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(username, null, UserRole.valueOf(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
                customOAuth2User.getAuthorities());
        // save a user in session temporarily
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
