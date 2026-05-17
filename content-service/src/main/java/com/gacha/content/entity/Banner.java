package com.gacha.content.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("gacha_banner")
public class Banner {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String imageUrl;

    private Integer targetType;

    private String targetData;

    private String platform;

    private Integer blockId;

    private Integer weight;

    private Integer status;

    private LocalDateTime effectiveTime;

    private LocalDateTime expireTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private LocalDateTime deletedAt;
}
