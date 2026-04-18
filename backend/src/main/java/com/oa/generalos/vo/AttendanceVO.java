package com.oa.generalos.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AttendanceVO {
    private Long id;
    private Long userId;
    private String userName;
    private Integer year;
    private Integer month;
    private Integer day;
    private String status;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
