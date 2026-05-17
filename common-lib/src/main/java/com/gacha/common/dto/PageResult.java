package com.gacha.common.dto;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class PageResult<T> {

    private List<T> list;
    private Long total;
    private Integer page;
    private Integer pageSize;

    private PageResult() {}

    public static <T> PageResult<T> of(List<T> list, Long total, Integer page, Integer pageSize) {
        PageResult<T> r = new PageResult<>();
        r.list = list != null ? list : Collections.emptyList();
        r.total = total != null ? total : 0L;
        r.page = page != null ? page : 1;
        r.pageSize = pageSize != null ? pageSize : 20;
        return r;
    }

    public static <T> PageResult<T> empty(Integer page, Integer pageSize) {
        return of(Collections.emptyList(), 0L, page, pageSize);
    }
}
