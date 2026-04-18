package com.oa.generalos.service;

import com.oa.generalos.vo.DinnerRecordVO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DinnerRecordService {
    List<DinnerRecordVO> getAllRecords();

    List<DinnerRecordVO> getRecordsByDateRange(LocalDate startDate, LocalDate endDate);

    List<DinnerRecordVO> getRecordsByDepartment(String department);

    DinnerRecordVO getRecordById(Long id);

    void createRecord(LocalDate recordDate, String department, Integer participantCount,
                     BigDecimal amount, String purpose, String responsiblePerson,
                     String invoicePath, Long createdBy);

    void updateRecord(Long id, LocalDate recordDate, String department,
                     Integer participantCount, BigDecimal amount, String purpose,
                     String responsiblePerson, String invoicePath);

    void deleteRecord(Long id);

    Map<String, Object> getStatistics(LocalDate startDate, LocalDate endDate);

    BigDecimal getTotalAmount(LocalDate startDate, LocalDate endDate);
}
