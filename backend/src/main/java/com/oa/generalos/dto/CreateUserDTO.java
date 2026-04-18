package com.oa.generalos.dto;

import lombok.Data;

@Data
public class CreateUserDTO {
    private String username;
    private String password;
    private String realName;
    private String role;
    private Long departmentId;
    private String email;
    private String phone;
}
