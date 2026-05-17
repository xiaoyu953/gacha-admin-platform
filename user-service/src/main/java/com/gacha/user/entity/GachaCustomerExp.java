package com.gacha.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("gacha_customer_exp_new")
public class GachaCustomerExp {

    @TableId(type = IdType.INPUT)
    private Integer level;

    private Integer exp;

    private Integer prizeId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
