package com.gacha.auth.controller;

import com.gacha.auth.dto.LoginRequest;
import com.gacha.auth.dto.LoginResponse;
import com.gacha.auth.service.AuthService;
import com.gacha.common.dto.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Result.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public Result<LoginResponse> refresh(@RequestParam String refreshToken) {
        return Result.ok(authService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        authService.logout(userId, jwt.getTokenValue());
        return Result.ok();
    }

    @GetMapping("/me")
    public Result<LoginResponse.UserInfo> me(@AuthenticationPrincipal Jwt jwt) {
        Long userId = Long.parseLong(jwt.getSubject());
        return Result.ok(authService.getUserInfo(userId));
    }
}
