package com.oa.generalos.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DictItem {
    private Long id;
    private Long categoryId;
    private String code;
    private String name;
    private String description;
    private Integer sortOrder;
    private Integer status;
    private String extra;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
