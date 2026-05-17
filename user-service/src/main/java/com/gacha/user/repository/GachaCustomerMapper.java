package com.gacha.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gacha.user.entity.GachaCustomer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GachaCustomerMapper extends BaseMapper<GachaCustomer> {
}
