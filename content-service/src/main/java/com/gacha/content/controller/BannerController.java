package com.gacha.content.controller;

import com.gacha.common.dto.PageResult;
import com.gacha.common.dto.Result;
import com.gacha.content.entity.Banner;
import com.gacha.content.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @GetMapping("/banners")
    public Result<PageResult<?>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        return Result.ok(bannerService.list(page, pageSize, keyword, status));
    }

    @GetMapping("/banners/{id}")
    public Result<?> get(@PathVariable Long id) {
        return Result.ok(bannerService.getById(id));
    }

    @PostMapping("/banners")
    public Result<?> create(@RequestBody Banner entity) {
        return Result.ok(bannerService.create(entity));
    }

    @PutMapping("/banners/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Banner entity) {
        return Result.ok(bannerService.update(id, entity));
    }

    @DeleteMapping("/banners/{id}")
    public Result<?> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return Result.ok();
    }
}
