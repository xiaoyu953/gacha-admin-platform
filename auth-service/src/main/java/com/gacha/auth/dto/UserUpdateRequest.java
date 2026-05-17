package com.gacha.auth.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserUpdateRequest {
    private String realName;
    private String email;
    private Integer status;
    private List<Long> roleIds;
}
