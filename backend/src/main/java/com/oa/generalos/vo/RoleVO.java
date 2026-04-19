package com.oa.generalos.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoleVO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> permissions;
    private Integer permissionCount;
}