package com.gacha.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gacha.common.dto.PageResult;
import com.gacha.common.exception.BusinessException;
import com.gacha.user.entity.GachaCustomer;
import com.gacha.user.entity.GachaCustomerBlacklist;
import com.gacha.user.repository.GachaCustomerBlacklistMapper;
import com.gacha.user.repository.GachaCustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GachaCustomerBlacklistService {

    private final GachaCustomerBlacklistMapper mapper;
    private final GachaCustomerMapper customerMapper;

    public PageResult<GachaCustomerBlacklist> list(int pageNum, int pageSize, Integer customerId, Integer type) {
        LambdaQueryWrapper<GachaCustomerBlacklist> qw = new LambdaQueryWrapper<>();
        qw.eq(customerId != null, GachaCustomerBlacklist::getCustomerId, customerId);
        qw.eq(type != null, GachaCustomerBlacklist::getType, type);
        qw.orderByDesc(GachaCustomerBlacklist::getId);
        Page<GachaCustomerBlacklist> page = mapper.selectPage(new Page<>(pageNum, pageSize), qw);

        List<GachaCustomerBlacklist> records = page.getRecords();
        if (!records.isEmpty()) {
            Set<Integer> customerIds = records.stream()
                    .map(GachaCustomerBlacklist::getCustomerId)
                    .collect(Collectors.toSet());
            Map<Integer, String> nickMap = customerMapper.selectBatchIds(customerIds).stream()
                    .collect(Collectors.toMap(GachaCustomer::getCustomerId, GachaCustomer::getNick, (a, b) -> a));
            records.forEach(r -> r.setCustomerNick(nickMap.get(r.getCustomerId())));
        }
        return PageResult.of(records, page.getTotal(), pageNum, pageSize);
    }

    public GachaCustomerBlacklist create(GachaCustomerBlacklist entity) {
        // check duplicate
        if (mapper.selectCount(new LambdaQueryWrapper<GachaCustomerBlacklist>()
                .eq(GachaCustomerBlacklist::getCustomerId, entity.getCustomerId())
                .eq(GachaCustomerBlacklist::getType, entity.getType())) > 0) {
            throw new BusinessException(400, "该用户已在此黑名单类型中");
        }
        mapper.insert(entity);
        return mapper.selectById(entity.getId());
    }

    @Transactional
    public List<GachaCustomerBlacklist> batchCreate(List<Integer> customerIds, Integer type) {
        List<GachaCustomerBlacklist> result = new ArrayList<>();
        for (Integer cid : customerIds) {
            GachaCustomerBlacklist entity = new GachaCustomerBlacklist();
            entity.setCustomerId(cid);
            entity.setType(type);
            try {
                result.add(create(entity));
            } catch (BusinessException ignored) {
                // skip duplicates
            }
        }
        return result;
    }

    public void delete(Long id) {
        if (mapper.deleteById(id) == 0) throw new BusinessException(404, "黑名单记录不存在");
    }

    @Transactional
    public void batchDelete(List<Long> ids) {
        mapper.deleteBatchIds(ids);
    }
}
