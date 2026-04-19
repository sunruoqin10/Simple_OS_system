package com.oa.generalos.vo;

import lombok.Data;
import java.util.List;

@Data
public class UserVO {
    private Long id;
    private String username;
    private String realName;
    private String role;
    private Long departmentId;
    private String departmentName;
    private String email;
    private String phone;
    private Integer mustChangePassword;
    private List<String> roles;
}