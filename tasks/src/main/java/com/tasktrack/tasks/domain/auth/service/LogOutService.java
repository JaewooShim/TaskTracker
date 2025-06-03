package com.tasktrack.tasks.domain.auth.service;

import com.tasktrack.tasks.domain.auth.repository.TokenRepository;
import com.tasktrack.tasks.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        if (request.getCookies() == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        for (Cookie cookie : request.getCookies()) {
            if ("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
                break;
            }
        }

        // refresh null check
        if (refresh == null) {
    //            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!jwtUtil.getCategory(refresh).equals("refresh")) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!tokenRepository.existsByRefresh(refresh)) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        tokenRepository.deleteByRefresh(refresh);

        deleteCookie(response, "refresh");
        deleteCookie(response, "access");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
