package com.oa.generalos.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private String realName;
    private String role;
    private Long departmentId;
    private String email;
    private String phone;
    private Integer status;
}
