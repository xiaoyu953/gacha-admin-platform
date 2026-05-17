package com.gacha.auth.controller;

import com.gacha.auth.dto.UserCreateRequest;
import com.gacha.auth.dto.UserUpdateRequest;
import com.gacha.auth.service.UserService;
import com.gacha.common.dto.PageResult;
import com.gacha.common.dto.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<PageResult<?>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.ok(userService.list(page, pageSize, keyword));
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> get(@PathVariable Long id) {
        return Result.ok(userService.getById(id));
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> create(@Valid @RequestBody UserCreateRequest req) {
        return Result.ok(userService.create(req));
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> update(@PathVariable Long id, @RequestBody UserUpdateRequest req) {
        return Result.ok(userService.update(id, req));
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public Result<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.ok();
    }
}
