package com.gacha.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("gacha_customer_blacklist")
public class GachaCustomerBlacklist {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer customerId;

    private Integer type;

    private Integer adminId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private String customerNick;
}
