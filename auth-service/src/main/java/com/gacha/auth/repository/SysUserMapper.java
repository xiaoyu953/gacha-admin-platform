package com.gacha.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gacha.auth.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT r.role_code FROM sys_user_role ur " +
            "JOIN sys_role r ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND r.deleted_at IS NULL")
    List<String> findRoleCodesByUserId(Long userId);

    @Select("SELECT p.perm_code FROM sys_role_permission rp " +
            "JOIN sys_user_role ur ON ur.role_id = rp.role_id " +
            "JOIN sys_permission p ON p.id = rp.permission_id " +
            "WHERE ur.user_id = #{userId}")
    List<String> findPermCodesByUserId(Long userId);
}
