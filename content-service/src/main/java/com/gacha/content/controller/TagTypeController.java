package com.gacha.content.controller;

import com.gacha.common.dto.PageResult;
import com.gacha.common.dto.Result;
import com.gacha.content.entity.TagType;
import com.gacha.content.service.TagTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class TagTypeController {

    private final TagTypeService tagTypeService;

    @GetMapping("/tag-types")
    public Result<PageResult<?>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.ok(tagTypeService.list(page, pageSize, keyword));
    }

    @GetMapping("/tag-types/all")
    public Result<?> all() {
        return Result.ok(tagTypeService.all());
    }

    @GetMapping("/tag-types/{id}")
    public Result<?> get(@PathVariable Long id) {
        return Result.ok(tagTypeService.getById(id));
    }

    @PostMapping("/tag-types")
    public Result<?> create(@RequestBody TagType entity) {
        return Result.ok(tagTypeService.create(entity));
    }

    @PutMapping("/tag-types/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody TagType entity) {
        return Result.ok(tagTypeService.update(id, entity));
    }

    @DeleteMapping("/tag-types/{id}")
    public Result<?> delete(@PathVariable Long id) {
        tagTypeService.delete(id);
        return Result.ok();
    }
}
