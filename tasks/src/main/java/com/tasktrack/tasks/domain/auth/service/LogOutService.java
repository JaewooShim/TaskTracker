package com.tasktrack.tasks.domain.auth.service;

import com.tasktrack.tasks.domain.auth.repository.TokenRepository;
import com.tasktrack.tasks.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class LogOutService {

    private final JWTUtil jwtUtil;

    private final TokenRepository tokenRepository;

    public LogOutService(JWTUtil jwtUtil, TokenRepository tokenRepository) {
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // get refresh token
        String refresh = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
                break;
            }
        }

        // refresh null check
        if (refresh == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        // expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        if (!jwtUtil.getCategory(refresh).equals("refresh")) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }

        if (!tokenRepository.existsByRefresh(refresh)) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return;
        }
        tokenRepository.deleteByRefresh(refresh);

        deleteCookie(response, "refresh", "/");
        deleteCookie(response, "access", "/");
        response.setStatus(HttpStatus.OK.value());
    }
    private void deleteCookie(HttpServletResponse response, String name, String path) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath(path);
        response.addCookie(cookie);
    }
}
