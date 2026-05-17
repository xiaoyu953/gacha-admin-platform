package com.gacha.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gacha.common.dto.PageResult;
import com.gacha.common.exception.BusinessException;
import com.gacha.content.entity.Banner;
import com.gacha.content.repository.BannerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerMapper bannerMapper;

    public PageResult<Banner> list(int pageNum, int pageSize, String keyword, Integer status) {
        LambdaQueryWrapper<Banner> qw = new LambdaQueryWrapper<Banner>()
                .like(keyword != null, Banner::getTitle, keyword)
                .eq(status != null, Banner::getStatus, status)
                .orderByAsc(Banner::getWeight)
                .orderByDesc(Banner::getCreatedAt);
        Page<Banner> page = bannerMapper.selectPage(new Page<>(pageNum, pageSize), qw);
        return PageResult.of(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    public Banner getById(Long id) {
        Banner entity = bannerMapper.selectById(id);
        if (entity == null) throw new BusinessException(404, "Banner不存在");
        return entity;
    }

    public Banner create(Banner entity) {
        normalizePlatform(entity);
        normalizeTargetData(entity);
        validateTime(entity);
        bannerMapper.insert(entity);
        return entity;
    }

    public Banner update(Long id, Banner entity) {
        getById(id);
        entity.setId(id);
        normalizePlatform(entity);
        normalizeTargetData(entity);
        validateTime(entity);
        bannerMapper.updateById(entity);
        return bannerMapper.selectById(id);
    }

    public void delete(Long id) {
        getById(id);
        bannerMapper.deleteById(id);
    }

    private void normalizePlatform(Banner entity) {
        if (!StringUtils.hasText(entity.getPlatform())) return;
        String platform = entity.getPlatform().trim();
        List<String> platforms = Arrays.stream(platform.split("[,|]"))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
        if (platforms.contains("all")) {
            entity.setPlatform("all");
        } else {
            entity.setPlatform(String.join("|", platforms));
        }
    }

    private void normalizeTargetData(Banner entity) {
        if (entity.getTargetData() != null) {
            entity.setTargetData(entity.getTargetData().trim());
        }
    }

    private void validateTime(Banner entity) {
        LocalDateTime effective = entity.getEffectiveTime();
        LocalDateTime expire = entity.getExpireTime();
        if (effective != null && expire != null && expire.isBefore(effective)) {
            throw new BusinessException(400, "过期时间不能早于生效时间");
        }
    }
}
