package com.gacha.user.controller;

import com.gacha.common.dto.PageResult;
import com.gacha.common.dto.Result;
import com.gacha.user.entity.GachaCustomer;
import com.gacha.user.service.GachaCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class GachaCustomerController {

    private final GachaCustomerService service;

    @GetMapping("/customers")
    public Result<PageResult<?>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer ban) {
        return Result.ok(service.list(page, pageSize, keyword, ban));
    }

    @GetMapping("/customers/{id}")
    public Result<?> get(@PathVariable Integer id) {
        return Result.ok(service.getById(id));
    }

    @PostMapping("/customers")
    public Result<?> create(@RequestBody GachaCustomer entity) {
        return Result.ok(service.create(entity));
    }

    @PutMapping("/customers/{id}")
    public Result<?> update(@PathVariable Integer id, @RequestBody GachaCustomer entity) {
        return Result.ok(service.update(id, entity));
    }

    @DeleteMapping("/customers/{id}")
    public Result<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return Result.ok();
    }
}
