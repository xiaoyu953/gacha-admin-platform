package com.gacha.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gacha.auth.entity.SysRolePermission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    @Delete("DELETE FROM sys_role_permission WHERE role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") Long roleId);

    @Insert("<script>" +
            "INSERT INTO sys_role_permission (role_id, permission_id) VALUES " +
            "<foreach collection='permIds' item='permId' separator=','> (#{roleId}, #{permId}) </foreach>" +
            "</script>")
    void batchInsert(@Param("roleId") Long roleId, @Param("permIds") List<Long> permIds);
}
