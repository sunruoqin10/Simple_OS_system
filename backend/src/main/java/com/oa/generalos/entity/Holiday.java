package com.oa.generalos.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Holiday {
    private Long id;
    private Integer year;
    private Integer month;
    private Integer day;
    private String name;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
