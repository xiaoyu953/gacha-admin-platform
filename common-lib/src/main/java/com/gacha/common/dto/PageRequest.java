package com.gacha.common.dto;

import lombok.Data;

@Data
public class PageRequest {
    private Integer page = 1;
    private Integer pageSize = 20;

    public Integer getOffset() {
        return (page - 1) * pageSize;
    }
}
