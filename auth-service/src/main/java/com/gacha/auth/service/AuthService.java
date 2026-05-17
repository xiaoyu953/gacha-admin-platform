package com.gacha.auth.service;

import com.gacha.auth.dto.LoginRequest;
import com.gacha.auth.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse refresh(String refreshToken);
    void logout(Long userId, String accessToken);
    LoginResponse.UserInfo getUserInfo(Long userId);
}
