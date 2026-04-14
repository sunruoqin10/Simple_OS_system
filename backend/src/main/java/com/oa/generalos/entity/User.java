package com.oa.generalos.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String role;
    private Long departmentId;
    private String email;
    private String phone;
    private Integer status;
    private Integer errorCount;
    private LocalDateTime lockedUntil;
    private Integer mustChangePassword;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isLocked() {
        return lockedUntil != null && LocalDateTime.now().isBefore(lockedUntil);
    }

    public boolean isDisabled() {
        return status != null && status == 0;
    }

    public boolean mustChangePassword() {
        return mustChangePassword != null && mustChangePassword == 1;
    }
}
