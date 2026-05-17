package com.gacha.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gacha.auth.dto.LoginResponse;
import com.gacha.auth.entity.*;
import com.gacha.auth.repository.*;
import com.gacha.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final SysRoleMapper roleMapper;
    private final SysPermissionMapper permissionMapper;
    private final SysRolePermissionMapper rolePermissionMapper;
    private final SysUserMapper userMapper;
    private final SysUserRoleMapper userRoleMapper;

    public List<SysRole> list() {
        return roleMapper.selectList(new LambdaQueryWrapper<SysRole>().orderByAsc(SysRole::getId));
    }

    public SysRole create(String roleName, String roleCode, String description) {
        SysRole role = new SysRole();
        role.setRoleName(roleName);
        role.setRoleCode(roleCode);
        role.setDescription(description);
        roleMapper.insert(role);
        return role;
    }

    public SysRole update(Long id, String roleName, String description) {
        SysRole role = roleMapper.selectById(id);
        if (role == null) throw new BusinessException(404, "角色不存在");
        if (roleName != null) role.setRoleName(roleName);
        if (description != null) role.setDescription(description);
        roleMapper.updateById(role);
        return role;
    }

    @Transactional
    public void delete(Long id) {
        rolePermissionMapper.deleteByRoleId(id);
        roleMapper.deleteById(id);
    }

    @Transactional
    public void assignPermissions(Long roleId, List<Long> permIds) {
        rolePermissionMapper.deleteByRoleId(roleId);
        if (permIds != null && !permIds.isEmpty()) {
            rolePermissionMapper.batchInsert(roleId, permIds);
        }
    }

    public List<SysPermission> getRolePermissions(Long roleId) {
        List<SysRolePermission> rps = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId));
        if (rps.isEmpty()) return Collections.emptyList();
        List<Long> permIds = rps.stream().map(SysRolePermission::getPermissionId).toList();
        return permissionMapper.selectBatchIds(permIds);
    }

    public Map<String, Object> getRoleDetail(Long roleId) {
        SysRole role = roleMapper.selectById(roleId);
        if (role == null) throw new BusinessException(404, "角色不存在");
        List<SysRolePermission> rps = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<SysRolePermission>().eq(SysRolePermission::getRoleId, roleId));
        List<Long> permIds = rps.stream().map(SysRolePermission::getPermissionId).toList();
        List<SysUserRole> urs = userRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId));
        List<Long> userIds = urs.stream().map(SysUserRole::getUserId).toList();
        List<SysUser> users = userIds.isEmpty() ? Collections.emptyList()
                : userMapper.selectBatchIds(userIds);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("role", role);
        result.put("permissions", permIds.isEmpty() ? Collections.emptyList()
                : permissionMapper.selectBatchIds(permIds));
        result.put("users", users);
        return result;
    }

    public List<SysPermission> getAllPermissions() {
        return permissionMapper.selectList(new LambdaQueryWrapper<SysPermission>().orderByAsc(SysPermission::getSortOrder));
    }

    public SysPermission createPermission(String permName, String permCode, String permType,
                                           Long parentId, String path, String icon, Integer sortOrder) {
        SysPermission perm = new SysPermission();
        perm.setPermName(permName);
        perm.setPermCode(permCode);
        perm.setPermType(permType);
        perm.setParentId(parentId);
        perm.setPath(path);
        perm.setIcon(icon);
        perm.setSortOrder(sortOrder != null ? sortOrder : 0);
        permissionMapper.insert(perm);
        return perm;
    }

    public void deletePermission(Long id) {
        permissionMapper.deleteById(id);
    }
}
