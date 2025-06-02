package com.tasktrack.tasks.controller;

import com.tasktrack.tasks.domain.auth.service.LogOutService;
import com.tasktrack.tasks.domain.auth.service.ReissueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TokenController {

    private final ReissueService reissueService;

    private final LogOutService logOutService;

    public TokenController(ReissueService reissueService, LogOutService logOutService) {
        this.reissueService = reissueService;
        this.logOutService = logOutService;
    }

    @PostMapping("/util/token-reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        return reissueService.reissue(request, response);
    }

    @PostMapping("/util/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logOutService.logout(request, response);
    }

    @GetMapping("/auth")
    public ResponseEntity<?> authCheck(HttpServletRequest request, HttpServletResponse response) {
        return reissueService.checkAuth();
    }
}
