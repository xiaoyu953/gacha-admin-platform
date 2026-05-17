package com.gacha.content.controller;

import com.gacha.common.dto.PageResult;
import com.gacha.common.dto.Result;
import com.gacha.content.entity.Advert;
import com.gacha.content.service.AdvertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdvertController {

    private final AdvertService advertService;

    @GetMapping("/adverts")
    public Result<PageResult<?>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.ok(advertService.list(page, pageSize, keyword));
    }

    @GetMapping("/adverts/{id}")
    public Result<?> get(@PathVariable Long id) {
        return Result.ok(advertService.getById(id));
    }

    @PostMapping("/adverts")
    public Result<?> create(@RequestBody Advert entity) {
        return Result.ok(advertService.create(entity));
    }

    @PutMapping("/adverts/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Advert entity) {
        return Result.ok(advertService.update(id, entity));
    }

    @DeleteMapping("/adverts/{id}")
    public Result<?> delete(@PathVariable Long id) {
        advertService.delete(id);
        return Result.ok();
    }
}
