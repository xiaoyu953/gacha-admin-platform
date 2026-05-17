package com.gacha.auth.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gacha.auth.entity.*;
import com.gacha.auth.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SysUserMapper userMapper;
    private final SysRoleMapper roleMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userMapper.selectCount(new LambdaQueryWrapper<>()) > 0) {
            return;
        }
        log.info("Initializing default admin user...");

        SysUser admin = new SysUser();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRealName("超级管理员");
        admin.setStatus(1);
        userMapper.insert(admin);

        SysRole role = new SysRole();
        role.setRoleName("超级管理员");
        role.setRoleCode("SUPER_ADMIN");
        role.setDescription("拥有所有权限");
        roleMapper.insert(role);

        SysUserRole ur = new SysUserRole();
        ur.setUserId(admin.getId());
        ur.setRoleId(role.getId());
        userRoleMapper.insert(ur);

        log.info("Default admin created: admin/admin123");
    }
}
