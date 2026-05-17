package com.gacha.content.controller;

import com.gacha.common.dto.PageResult;
import com.gacha.common.dto.Result;
import com.gacha.content.entity.Tag;
import com.gacha.content.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/tags")
    public Result<PageResult<?>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.ok(tagService.list(page, pageSize, keyword));
    }

    @GetMapping("/tags/{id}")
    public Result<?> get(@PathVariable Long id) {
        return Result.ok(tagService.getById(id));
    }

    @PostMapping("/tags")
    public Result<?> create(@RequestBody Tag entity) {
        return Result.ok(tagService.create(entity));
    }

    @PutMapping("/tags/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Tag entity) {
        return Result.ok(tagService.update(id, entity));
    }

    @DeleteMapping("/tags/{id}")
    public Result<?> delete(@PathVariable Long id) {
        tagService.delete(id);
        return Result.ok();
    }
}
