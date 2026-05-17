package com.gacha.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gacha.common.dto.PageResult;
import com.gacha.common.exception.BusinessException;
import com.gacha.user.entity.GachaCustomer;
import com.gacha.user.repository.GachaCustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class GachaCustomerService {

    private final GachaCustomerMapper mapper;

    public PageResult<GachaCustomer> list(int pageNum, int pageSize, String keyword, Integer ban) {
        LambdaQueryWrapper<GachaCustomer> qw = new LambdaQueryWrapper<GachaCustomer>();
        if (StringUtils.hasText(keyword)) {
            Integer id = tryParseInt(keyword);
            qw.and(w -> {
                w.like(GachaCustomer::getNick, keyword)
                 .or()
                 .like(GachaCustomer::getMobile, keyword);
                if (id != null) {
                    w.or().eq(GachaCustomer::getCustomerId, id);
                }
            });
        }
        qw.eq(ban != null, GachaCustomer::getBan, ban)
          .orderByDesc(GachaCustomer::getCreatedAt);
        Page<GachaCustomer> page = mapper.selectPage(new Page<>(pageNum, pageSize), qw);
        return PageResult.of(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    private Integer tryParseInt(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public GachaCustomer getById(Integer id) {
        GachaCustomer entity = mapper.selectById(id);
        if (entity == null) throw new BusinessException(404, "用户不存在");
        return entity;
    }

    public GachaCustomer create(GachaCustomer entity) {
        mapper.insert(entity);
        return entity;
    }

    public GachaCustomer update(Integer id, GachaCustomer entity) {
        getById(id);
        entity.setCustomerId(id);
        mapper.updateById(entity);
        return mapper.selectById(id);
    }

    public void delete(Integer id) {
        getById(id);
        mapper.deleteById(id);
    }
}
