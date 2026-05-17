package com.gacha.content.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gacha.content.entity.Advert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdvertMapper extends BaseMapper<Advert> {
}
