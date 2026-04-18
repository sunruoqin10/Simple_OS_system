package com.oa.generalos.controller;

import com.oa.generalos.common.Result;
import com.oa.generalos.service.DinnerRecordService;
import com.oa.generalos.vo.DinnerRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dinner")
public class DinnerRecordController {

    @Autowired
    private DinnerRecordService dinnerRecordService;

    @GetMapping("/list")
    public Result<List<DinnerRecordVO>> getAllRecords() {
        List<DinnerRecordVO> records = dinnerRecordService.getAllRecords();
        return Result.success(records);
    }

    @GetMapping("/list/date-range")
    public Result<List<DinnerRecordVO>> getRecordsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<DinnerRecordVO> records = dinnerRecordService.getRecordsByDateRange(startDate, endDate);
        return Result.success(records);
    }

    @GetMapping("/list/department")
    public Result<List<DinnerRecordVO>> getRecordsByDepartment(
            @RequestParam String department) {
        List<DinnerRecordVO> records = dinnerRecordService.getRecordsByDepartment(department);
        return Result.success(records);
    }

    @GetMapping("/{id}")
    public Result<DinnerRecordVO> getRecordById(@PathVariable Long id) {
        DinnerRecordVO record = dinnerRecordService.getRecordById(id);
        return Result.success(record);
    }

    @PostMapping("/create")
    public Result<Void> createRecord(@RequestBody Map<String, Object> request) {
        LocalDate recordDate = LocalDate.parse(request.get("recordDate").toString());
        String department = (String) request.get("department");
        Integer participantCount = Integer.valueOf(request.get("participantCount").toString());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String purpose = (String) request.get("purpose");
        String responsiblePerson = (String) request.get("responsiblePerson");
        String invoicePath = (String) request.get("invoicePath");
        Long createdBy = Long.valueOf(request.get("createdBy").toString());

        dinnerRecordService.createRecord(recordDate, department, participantCount,
                amount, purpose, responsiblePerson, invoicePath, createdBy);
        return Result.success();
    }

    @PutMapping("/update/{id}")
    public Result<Void> updateRecord(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        LocalDate recordDate = LocalDate.parse(request.get("recordDate").toString());
        String department = (String) request.get("department");
        Integer participantCount = Integer.valueOf(request.get("participantCount").toString());
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        String purpose = (String) request.get("purpose");
        String responsiblePerson = (String) request.get("responsiblePerson");
        String invoicePath = (String) request.get("invoicePath");

        dinnerRecordService.updateRecord(id, recordDate, department, participantCount,
                amount, purpose, responsiblePerson, invoicePath);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteRecord(@PathVariable Long id) {
        dinnerRecordService.deleteRecord(id);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        Map<String, Object> statistics = dinnerRecordService.getStatistics(startDate, endDate);
        return Result.success(statistics);
    }

    @GetMapping("/total-amount")
    public Result<BigDecimal> getTotalAmount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal totalAmount = dinnerRecordService.getTotalAmount(startDate, endDate);
        return Result.success(totalAmount);
    }
}
