package com.tasktrack.tasks.domain.auth.service;

import com.tasktrack.tasks.domain.auth.entity.TokenEntity;
import com.tasktrack.tasks.domain.auth.repository.TokenRepository;
import com.tasktrack.tasks.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ReissueService {

    private final JWTUtil jwtUtil;

    private final TokenRepository tokenRepository;

    public ReissueService(JWTUtil jwtUtil, TokenRepository tokenRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;

        if (request.getCookies() == null) {
            return new ResponseEntity<>("please sign in again", HttpStatus.BAD_REQUEST);
        }

        for (Cookie cookie: request.getCookies()) {
            if ("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
                break;
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("please sign in again", HttpStatus.BAD_REQUEST);
        }

        // expiration check
        try {
            jwtUtil.isExpired(refresh);
        } catch (Exception e) {
            if (tokenRepository.existsByRefresh(refresh)) tokenRepository.deleteByRefresh(refresh);
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // check category or stored in db
        if (!jwtUtil.getCategory(refresh).equals("refresh") || !tokenRepository.existsByRefresh(refresh)) {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.generateJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.generateJwt("refresh", username, role, 43200000L);

        // delete the current refresh token
        tokenRepository.deleteByRefresh(refresh);

        // save newly created refresh token into db
        TokenEntity tokenEntity = TokenEntity.builder()
                .username(username)
                .refresh(newRefresh)
                .expiration(new Date(System.currentTimeMillis() + 43200000L).toString())
                .build();

        tokenRepository.save(tokenEntity);

        // response
//        response.setHeader("access", newAccess);
        response.addCookie(createCookie("access", newAccess, 1200));
        response.addCookie(createCookie("refresh", newRefresh, 43200)); // refresh rotation
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // check whether the current user is logged in
    public ResponseEntity<?> checkAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
        !(authentication instanceof AnonymousAuthenticationToken)) {
            return new ResponseEntity<>("Authenticated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Not authenticated", HttpStatus.BAD_REQUEST);
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
