package com.gacha.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private Boolean success;
    private Integer code;
    private String msg;
    private T data;

    private Result() {}

    public static <T> Result<T> ok(T data) {
        Result<T> r = new Result<>();
        r.success = true;
        r.code = 0;
        r.msg = "success";
        r.data = data;
        return r;
    }

    public static <T> Result<T> ok() {
        return ok(null);
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> r = new Result<>();
        r.success = false;
        r.code = code;
        r.msg = msg;
        return r;
    }

    public static <T> Result<T> fail(String msg) {
        return fail(1, msg);
    }

    public static <T> Result<T> unauthorized() {
        return fail(401, "未登录或登录已过期");
    }

    public static <T> Result<T> forbidden() {
        return fail(403, "没有权限");
    }

    public static <T> Result<T> notFound() {
        return fail(404, "资源不存在");
    }
}
