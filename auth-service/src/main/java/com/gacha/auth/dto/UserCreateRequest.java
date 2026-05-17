package com.gacha.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class UserCreateRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 64)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 128)
    private String password;

    private String realName;
    private String email;

    private List<Long> roleIds;
}
