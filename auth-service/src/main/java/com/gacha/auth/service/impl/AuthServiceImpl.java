package com.gacha.auth.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.gacha.auth.dto.LoginRequest;
import com.gacha.auth.dto.LoginResponse;
import com.gacha.auth.entity.SysUser;
import com.gacha.auth.repository.SysUserMapper;
import com.gacha.auth.service.AuthService;
import com.gacha.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserMapper userMapper;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        SysUser user = userMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, request.getUsername())
        );
        if (user == null || !BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        List<String> roles = userMapper.findRoleCodesByUserId(user.getId());
        List<String> permissions = userMapper.findPermCodesByUserId(user.getId());

        String accessToken = generateAccessToken(user, roles);
        String refreshToken = generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(7200L)
                .userInfo(LoginResponse.UserInfo.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .realName(user.getRealName())
                        .roles(roles)
                        .permissions(permissions)
                        .build())
                .build();
    }

    @Override
    public LoginResponse refresh(String refreshToken) {
        try {
            Jwt jwt = jwtDecoder.decode(refreshToken);
            Long userId = Long.parseLong(jwt.getSubject());
            SysUser user = userMapper.selectById(userId);
            if (user == null || (user.getStatus() != null && user.getStatus() == 0)) {
                throw new BusinessException(401, "用户不存在或已禁用");
            }
            List<String> roles = userMapper.findRoleCodesByUserId(userId);
            List<String> permissions = userMapper.findPermCodesByUserId(userId);

            return LoginResponse.builder()
                    .accessToken(generateAccessToken(user, roles))
                    .refreshToken(generateRefreshToken(user))
                    .expiresIn(7200L)
                    .userInfo(LoginResponse.UserInfo.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .realName(user.getRealName())
                            .roles(roles)
                            .permissions(permissions)
                            .build())
                    .build();
        } catch (JwtException e) {
            throw new BusinessException(401, "refresh_token 无效或已过期");
        }
    }

    @Override
    public void logout(Long userId, String accessToken) {
        log.info("User {} logged out", userId);
    }

    @Override
    public LoginResponse.UserInfo getUserInfo(Long userId) {
        SysUser user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        List<String> roles = userMapper.findRoleCodesByUserId(userId);
        List<String> permissions = userMapper.findPermCodesByUserId(userId);

        return LoginResponse.UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    private String generateAccessToken(SysUser user, List<String> roles) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("gacha-auth")
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(7200))
                .claim("username", user.getUsername())
                .claim("roles", roles)
                .id(UUID.randomUUID().toString())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    private String generateRefreshToken(SysUser user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("gacha-auth")
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(604800))
                .id(UUID.randomUUID().toString())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
