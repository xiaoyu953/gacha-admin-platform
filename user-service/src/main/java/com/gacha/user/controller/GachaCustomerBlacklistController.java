package com.gacha.user.controller;

import com.gacha.common.dto.PageResult;
import com.gacha.common.dto.Result;
import com.gacha.user.entity.GachaCustomerBlacklist;
import com.gacha.user.service.GachaCustomerBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/customer-blacklist")
@RequiredArgsConstructor
public class GachaCustomerBlacklistController {

    private final GachaCustomerBlacklistService service;

    @GetMapping
    public Result<PageResult<GachaCustomerBlacklist>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) Integer type) {
        return Result.ok(service.list(page, pageSize, customerId, type));
    }

    @PostMapping
    public Result<GachaCustomerBlacklist> create(@RequestBody GachaCustomerBlacklist entity) {
        return Result.ok(service.create(entity));
    }

    @PostMapping("/batch")
    public Result<List<GachaCustomerBlacklist>> batchCreate(@RequestBody Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        List<Integer> ids = (List<Integer>) payload.get("ids");
        Integer type = (Integer) payload.get("type");
        return Result.ok(service.batchCreate(ids, type));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.ok();
    }

    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        List<Long> ids = payload.get("ids") instanceof List ?
                ((List<?>) payload.get("ids")).stream().map(o -> ((Number) o).longValue()).toList() :
                List.of();
        service.batchDelete(ids);
        return Result.ok();
    }
}
