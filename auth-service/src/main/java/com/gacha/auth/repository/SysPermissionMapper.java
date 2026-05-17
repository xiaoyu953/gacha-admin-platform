package com.gacha.auth.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gacha.auth.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
}
