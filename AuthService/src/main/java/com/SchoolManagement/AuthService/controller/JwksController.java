package com.SchoolManagement.AuthService.controller;

import com.SchoolManagement.AuthService.security.JwtUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/oauth2")
public class JwksController {

    private final JwtUtil jwtUtil;

    public JwksController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/jwks")
    public Map<String, Object> getJwks() {
        return jwtUtil.getPublicJWK();
    }
}

