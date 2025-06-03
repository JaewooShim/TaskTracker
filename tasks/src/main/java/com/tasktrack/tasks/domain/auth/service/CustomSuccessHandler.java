package com.tasktrack.tasks.domain.auth.service;

import com.tasktrack.tasks.domain.auth.dto.CustomOAuth2User;
import com.tasktrack.tasks.domain.auth.entity.TokenEntity;
import com.tasktrack.tasks.domain.auth.repository.TokenRepository;
import com.tasktrack.tasks.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    private final TokenRepository tokenRepository;

    public CustomSuccessHandler(JWTUtil jwtUtil, TokenRepository tokenRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String username = customOAuth2User.getUserName();

        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // generate access(10min) nd refresh(12hr) token
        String access = jwtUtil.generateJwt("access", username, role, 600000L);
        String refresh = jwtUtil.generateJwt("refresh", username, role, 43200000L);

        // save refresh token in db
        createTokenEntity(username, refresh, 43200000L);

        response.addCookie(createCookie("access", access, 1200));
//        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh, 43200));
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect("http://localhost:5173/");
    }

    private void createTokenEntity(String username, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);
        TokenEntity tokenEntity = TokenEntity.builder()
                .username(username)
                .refresh(refresh)
                .expiration(date.toString())
                .build();
        tokenRepository.save(tokenEntity);
    }

    private Cookie createCookie(String key, String value, int expiry) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiry);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
