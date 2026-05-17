package com.gacha.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gacha.common.dto.PageResult;
import com.gacha.common.exception.BusinessException;
import com.gacha.content.entity.Advert;
import com.gacha.content.repository.AdvertMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertService {

    private final AdvertMapper advertMapper;

    public PageResult<Advert> list(int pageNum, int pageSize, String keyword) {
        LambdaQueryWrapper<Advert> qw = new LambdaQueryWrapper<Advert>()
                .like(keyword != null, Advert::getTitle, keyword)
                .orderByDesc(Advert::getCreatedAt);
        Page<Advert> page = advertMapper.selectPage(new Page<>(pageNum, pageSize), qw);
        return PageResult.of(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    public Advert getById(Long id) {
        Advert entity = advertMapper.selectById(id);
        if (entity == null) throw new BusinessException(404, "广告不存在");
        return entity;
    }

    public Advert create(Advert entity) {
        advertMapper.insert(entity);
        return entity;
    }

    public Advert update(Long id, Advert entity) {
        getById(id);
        entity.setId(id);
        advertMapper.updateById(entity);
        return advertMapper.selectById(id);
    }

    public void delete(Long id) {
        getById(id);
        advertMapper.deleteById(id);
    }
}
