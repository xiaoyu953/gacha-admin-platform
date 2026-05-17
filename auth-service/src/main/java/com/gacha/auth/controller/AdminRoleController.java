package com.gacha.auth.controller;

import com.gacha.auth.service.RoleService;
import com.gacha.common.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRoleController {

    private final RoleService roleService;

    @GetMapping("/roles")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<List<?>> list() {
        return Result.ok(roleService.list());
    }

    @GetMapping("/roles/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        return Result.ok(roleService.getRoleDetail(id));
    }

    @PostMapping("/roles")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> create(@RequestBody Map<String, String> body) {
        return Result.ok(roleService.create(
                body.get("roleName"), body.get("roleCode"), body.get("description")));
    }

    @PutMapping("/roles/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> update(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return Result.ok(roleService.update(id, body.get("roleName"), body.get("description")));
    }

    @DeleteMapping("/roles/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.ok();
    }

    @PutMapping("/roles/{id}/permissions")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> assignPermissions(@PathVariable Long id, @RequestBody Map<String, List<Long>> body) {
        roleService.assignPermissions(id, body.get("permissionIds"));
        return Result.ok();
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> permissions() {
        return Result.ok(roleService.getAllPermissions());
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> createPermission(@RequestBody Map<String, Object> body) {
        return Result.ok(roleService.createPermission(
                (String) body.get("permName"), (String) body.get("permCode"),
                (String) body.get("permType"), body.get("parentId") != null
                        ? Long.valueOf(body.get("parentId").toString()) : null,
                (String) body.get("path"), (String) body.get("icon"),
                body.get("sortOrder") != null
                        ? Integer.valueOf(body.get("sortOrder").toString()) : null));
    }

    @DeleteMapping("/permissions/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> deletePermission(@PathVariable Long id) {
        roleService.deletePermission(id);
        return Result.ok();
    }
}
