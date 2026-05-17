package com.gacha.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gacha.common.dto.PageResult;
import com.gacha.common.exception.BusinessException;
import com.gacha.content.entity.TagType;
import com.gacha.content.repository.TagTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagTypeService {

    private final TagTypeMapper tagTypeMapper;

    public PageResult<TagType> list(int pageNum, int pageSize, String keyword) {
        LambdaQueryWrapper<TagType> qw = new LambdaQueryWrapper<TagType>()
                .like(keyword != null, TagType::getName, keyword)
                .orderByAsc(TagType::getWeight)
                .orderByDesc(TagType::getCreatedAt);
        Page<TagType> page = tagTypeMapper.selectPage(new Page<>(pageNum, pageSize), qw);
        return PageResult.of(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    public TagType getById(Long id) {
        TagType entity = tagTypeMapper.selectById(id);
        if (entity == null) throw new BusinessException(404, "标签类型不存在");
        return entity;
    }

    public TagType create(TagType entity) {
        tagTypeMapper.insert(entity);
        return entity;
    }

    public TagType update(Long id, TagType entity) {
        getById(id);
        entity.setId(id);
        tagTypeMapper.updateById(entity);
        return tagTypeMapper.selectById(id);
    }

    public void delete(Long id) {
        getById(id);
        tagTypeMapper.deleteById(id);
    }

    public java.util.List<TagType> all() {
        return tagTypeMapper.selectList(new LambdaQueryWrapper<TagType>()
                .orderByAsc(TagType::getWeight));
    }
}
