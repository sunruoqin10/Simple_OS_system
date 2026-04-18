package com.oa.generalos.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DictCategory {
    private Long id;
    private String code;
    private String name;
    private Long parentId;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
