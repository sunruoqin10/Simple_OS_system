package com.oa.generalos.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DinnerRecord {
    private Long id;
    private LocalDate recordDate;
    private String department;
    private Integer participantCount;
    private BigDecimal amount;
    private String purpose;
    private String responsiblePerson;
    private String invoicePath;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
