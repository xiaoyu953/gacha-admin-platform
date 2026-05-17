package com.gacha.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gacha.common.dto.PageResult;
import com.gacha.common.exception.BusinessException;
import com.gacha.content.entity.Tag;
import com.gacha.content.entity.TagType;
import com.gacha.content.repository.TagMapper;
import com.gacha.content.repository.TagTypeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagMapper tagMapper;
    private final TagTypeMapper tagTypeMapper;

    public PageResult<Tag> list(int pageNum, int pageSize, String keyword) {
        LambdaQueryWrapper<Tag> qw = new LambdaQueryWrapper<Tag>()
                .like(keyword != null, Tag::getTitle, keyword)
                .orderByDesc(Tag::getCreatedAt);
        Page<Tag> page = tagMapper.selectPage(new Page<>(pageNum, pageSize), qw);

        // populate typeName
        if (!page.getRecords().isEmpty()) {
            List<Long> typeIds = page.getRecords().stream()
                    .map(Tag::getTypeId)
                    .filter(id -> id != null)
                    .distinct()
                    .collect(Collectors.toList());
            if (!typeIds.isEmpty()) {
                List<TagType> types = tagTypeMapper.selectBatchIds(typeIds);
                Map<Long, String> nameMap = types.stream()
                        .collect(Collectors.toMap(TagType::getId, TagType::getName));
                page.getRecords().forEach(tag -> {
                    if (tag.getTypeId() != null) {
                        tag.setTypeName(nameMap.get(tag.getTypeId()));
                    }
                });
            }
        }

        return PageResult.of(page.getRecords(), page.getTotal(), pageNum, pageSize);
    }

    public Tag getById(Long id) {
        Tag entity = tagMapper.selectById(id);
        if (entity == null) throw new BusinessException(404, "标签不存在");
        if (entity.getTypeId() != null) {
            TagType type = tagTypeMapper.selectById(entity.getTypeId());
            if (type != null) entity.setTypeName(type.getName());
        }
        return entity;
    }

    public Tag create(Tag entity) {
        tagMapper.insert(entity);
        return entity;
    }

    public Tag update(Long id, Tag entity) {
        getById(id);
        entity.setId(id);
        tagMapper.updateById(entity);
        return tagMapper.selectById(id);
    }

    public void delete(Long id) {
        getById(id);
        tagMapper.deleteById(id);
    }
}
