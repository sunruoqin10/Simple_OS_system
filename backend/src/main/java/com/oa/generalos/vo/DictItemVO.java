package com.oa.generalos.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DictItemVO {
    private Long id;
    private Long categoryId;
    private String categoryCode;
    private String categoryName;
    private String code;
    private String name;
    private String description;
    private Integer sortOrder;
    private Integer status;
    private String extra;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
