package com.gacha.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("gacha_customer")
public class GachaCustomer {

    @TableId(type = IdType.INPUT)
    private Integer customerId;

    private String nick;

    private String mobile;

    private String wechatName;

    private String image;

    private BigDecimal gold;

    private Integer points;

    private Integer exp;

    private Integer level;

    private Integer signIn;

    private Integer luck;

    private Integer onceCny;

    private Integer cny;

    private Integer sex;

    private Integer channel;

    private Integer advert;

    private Integer subscription;

    private Integer ban;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private LocalDateTime deletedAt;
}
