package com.oa.generalos.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PermissionVO {
    private Long id;
    private String code;
    private String name;
    private String category;
    private String description;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}