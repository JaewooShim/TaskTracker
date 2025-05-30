package com.tasktrack.tasks.domain.auth.service;

import com.tasktrack.tasks.domain.auth.dto.CustomOAuth2User;
import com.tasktrack.tasks.domain.auth.entity.UserEntity;
import com.tasktrack.tasks.domain.auth.entity.UserRole;
import com.tasktrack.tasks.domain.auth.repository.UserRepository;
import com.tasktrack.tasks.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class JWTFilter extends OncePerRequestFilter {
    private static final String[] whitelist = {"/api/util/token-reissue", "/api/util/logout", "/"};

    private final JWTUtil jwtUtil;

    private final UserRepository userRepository;

    public JWTFilter(JWTUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // get access token
//        String accessToken = request.getHeader("access");
        String accessToken = null;
        for (Cookie cookie: request.getCookies()) {
            if (cookie.getName().equals("access")) {
                accessToken = cookie.getValue();
                break;
            }
        }
        if (accessToken == null) { // if access token doesn't exist, move on to the next filter
            filterChain.doFilter(request, response);
            return;
        }

        // if token expired, stop
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            PrintWriter writer = response.getWriter();
            writer.print("Invalid access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);

        // this user not in db
        if (optionalUser.isEmpty()) {
            PrintWriter writer = response.getWriter();
            writer.print("Invalid user");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UserEntity userEntity = optionalUser.get();

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userEntity);

        Authentication authentication = new UsernamePasswordAuthenticationToken(customOAuth2User
        ,null, customOAuth2User.getAuthorities());

        // save a user in session temporarily
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
