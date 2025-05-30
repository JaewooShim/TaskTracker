package com.tasktrack.tasks.domain.auth.service;

import com.tasktrack.tasks.domain.auth.entity.TokenEntity;
import com.tasktrack.tasks.domain.auth.repository.TokenRepository;
import com.tasktrack.tasks.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.Optional;

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
        // get refresh token
        String refresh = null;

        for (Cookie cookie: request.getCookies()) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
                break;
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token cannot be found", HttpStatus.BAD_REQUEST);
        }

        // expiration check
        try {
            jwtUtil.isExpired(refresh);
        } catch (Exception e) {
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
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUsername(username);
        tokenEntity.setRefresh(newRefresh);
        tokenEntity.setExpiration(new Date(System.currentTimeMillis() + 43200000L).toString());
        tokenRepository.save(tokenEntity);

        // response
//        response.setHeader("access", newAccess);
        response.addCookie(createCookie("access", newAccess));
        response.addCookie(createCookie("refresh", newRefresh)); // refresh rotation

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(12 * 60 * 60);
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }
}
