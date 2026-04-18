package com.oa.generalos.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Attendance {
    private Long id;
    private Long userId;
    private Integer year;
    private Integer month;
    private Integer day;
    private String status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
