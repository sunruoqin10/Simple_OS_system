package com.oa.generalos.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DepartmentVO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
