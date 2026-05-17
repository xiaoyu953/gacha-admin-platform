package com.gacha.auth.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponse {

    private String accessToken;

    private String refreshToken;

    private Long expiresIn;

    private UserInfo userInfo;

    @Data
    @Builder
    public static class UserInfo {
        private Long id;
        private String username;
        private String realName;
        private List<String> roles;
        private List<String> permissions;
    }
}
