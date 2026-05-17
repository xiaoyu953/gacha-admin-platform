package com.gacha.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gacha.auth.dto.UserCreateRequest;
import com.gacha.auth.dto.UserUpdateRequest;
import com.gacha.auth.entity.SysUser;
import com.gacha.auth.entity.SysUserRole;
import com.gacha.auth.repository.SysUserMapper;
import com.gacha.auth.repository.SysUserRoleMapper;
import com.gacha.common.dto.PageResult;
import com.gacha.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;

    public PageResult<SysUser> list(int pageNum, int pageSize, String keyword) {
        LambdaQueryWrapper<SysUser> qw = new LambdaQueryWrapper<SysUser>()
                .like(keyword != null, SysUser::getUsername, keyword)
                .or().like(keyword != null, SysUser::getRealName, keyword)
                .orderByDesc(SysUser::getCreatedAt);
        Page<SysUser> page = userMapper.selectPage(new Page<>(pageNum, pageSize), qw);
        return PageResult.of(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    public SysUser getById(Long id) {
        SysUser user = userMapper.selectById(id);
        if (user == null) throw new BusinessException(404, "用户不存在");
        return user;
    }

    @Transactional
    public SysUser create(UserCreateRequest req) {
        if (userMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, req.getUsername())) != null) {
            throw new BusinessException("用户名已存在");
        }
        SysUser user = new SysUser();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setRealName(req.getRealName());
        user.setEmail(req.getEmail());
        user.setStatus(1);
        userMapper.insert(user);

        if (req.getRoleIds() != null && !req.getRoleIds().isEmpty()) {
            userRoleMapper.batchInsert(user.getId(), req.getRoleIds());
        }
        return user;
    }

    @Transactional
    public SysUser update(Long id, UserUpdateRequest req) {
        SysUser user = getById(id);
        if (req.getRealName() != null) user.setRealName(req.getRealName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());
        if (req.getStatus() != null) user.setStatus(req.getStatus());
        userMapper.updateById(user);

        if (req.getRoleIds() != null) {
            userRoleMapper.deleteByUserId(id);
            if (!req.getRoleIds().isEmpty()) {
                userRoleMapper.batchInsert(id, req.getRoleIds());
            }
        }
        return user;
    }

    @Transactional
    public void delete(Long id) {
        SysUser user = getById(id);
        if ("admin".equals(user.getUsername())) {
            throw new BusinessException("不能删除超级管理员");
        }
        userRoleMapper.deleteByUserId(id);
        userMapper.deleteById(id);
    }

    public List<String> getUserRoleCodes(Long userId) {
        return userMapper.findRoleCodesByUserId(userId);
    }
}
