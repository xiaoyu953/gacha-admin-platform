package com.gacha.user.controller;

import com.gacha.common.dto.PageResult;
import com.gacha.common.dto.Result;
import com.gacha.user.entity.GachaCustomerExp;
import com.gacha.user.service.GachaCustomerExpService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/customer-exps")
@RequiredArgsConstructor
public class GachaCustomerExpController {

    private final GachaCustomerExpService service;

    @GetMapping
    public Result<PageResult<GachaCustomerExp>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize) {
        return Result.ok(service.list(page, pageSize));
    }

    @GetMapping("/{level}")
    public Result<GachaCustomerExp> get(@PathVariable Integer level) {
        return Result.ok(service.getById(level));
    }

    @PostMapping
    public Result<GachaCustomerExp> create(@RequestBody GachaCustomerExp entity) {
        return Result.ok(service.create(entity));
    }

    @PutMapping("/{level}")
    public Result<GachaCustomerExp> update(@PathVariable Integer level, @RequestBody GachaCustomerExp entity) {
        return Result.ok(service.update(level, entity));
    }

    @DeleteMapping("/{level}")
    public Result<Void> delete(@PathVariable Integer level) {
        service.delete(level);
        return Result.ok();
    }
}
