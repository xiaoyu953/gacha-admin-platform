package com.gacha.content.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("gacha_tag")
public class Tag {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private Long typeId;

    private Integer weight;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private LocalDateTime deletedAt;

    @TableField(exist = false)
    private String typeName;
}
