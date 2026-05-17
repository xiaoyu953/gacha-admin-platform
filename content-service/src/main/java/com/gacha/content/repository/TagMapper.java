package com.gacha.content.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gacha.content.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
