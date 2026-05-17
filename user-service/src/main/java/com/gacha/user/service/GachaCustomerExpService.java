package com.gacha.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gacha.common.dto.PageResult;
import com.gacha.common.exception.BusinessException;
import com.gacha.user.entity.GachaCustomerExp;
import com.gacha.user.repository.GachaCustomerExpMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GachaCustomerExpService {

    private final GachaCustomerExpMapper mapper;

    public PageResult<GachaCustomerExp> list(int pageNum, int pageSize) {
        LambdaQueryWrapper<GachaCustomerExp> qw = new LambdaQueryWrapper<>();
        qw.orderByAsc(GachaCustomerExp::getLevel);
        Page<GachaCustomerExp> page = mapper.selectPage(new Page<>(pageNum, pageSize), qw);
        return PageResult.of(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    public GachaCustomerExp getById(Integer level) {
        GachaCustomerExp entity = mapper.selectById(level);
        if (entity == null) throw new BusinessException(404, "等级配置不存在");
        return entity;
    }

    public GachaCustomerExp create(GachaCustomerExp entity) {
        if (mapper.selectById(entity.getLevel()) != null) {
            throw new BusinessException(400, "该等级已存在");
        }
        mapper.insert(entity);
        return entity;
    }

    public GachaCustomerExp update(Integer level, GachaCustomerExp entity) {
        getById(level);
        entity.setLevel(level);
        mapper.updateById(entity);
        return mapper.selectById(level);
    }

    public void delete(Integer level) {
        getById(level);
        mapper.deleteById(level);
    }
}
