package com.oa.generalos.controller;

import com.oa.generalos.common.Result;
import com.oa.generalos.service.AttendanceService;
import com.oa.generalos.vo.AttendanceVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/list")
    public Result<List<AttendanceVO>> getAllAttendances() {
        List<AttendanceVO> attendances = attendanceService.getAllAttendances();
        return Result.success(attendances);
    }

    @GetMapping("/user/{userId}")
    public Result<List<AttendanceVO>> getAttendancesByUserId(@PathVariable Long userId) {
        List<AttendanceVO> attendances = attendanceService.getAttendancesByUserId(userId);
        return Result.success(attendances);
    }

    @GetMapping("/calendar")
    public Result<List<AttendanceVO>> getAttendancesByYearMonth(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long userId) {
        List<AttendanceVO> attendances;
        if (userId != null) {
            attendances = attendanceService.getAttendancesByUserId(userId).stream()
                    .filter(a -> a.getYear().equals(year) && a.getMonth().equals(month))
                    .toList();
        } else {
            attendances = attendanceService.getAttendancesByYearMonth(year, month);
        }
        return Result.success(attendances);
    }

    @GetMapping("/{id}")
    public Result<AttendanceVO> getAttendanceById(@PathVariable Long id) {
        AttendanceVO attendance = attendanceService.getAttendanceById(id);
        return Result.success(attendance);
    }

    @PostMapping("/create")
    public Result<Void> createAttendance(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Integer year = Integer.valueOf(request.get("year").toString());
        Integer month = Integer.valueOf(request.get("month").toString());
        Integer day = Integer.valueOf(request.get("day").toString());
        String status = (String) request.get("status");
        String remark = (String) request.get("remark");

        attendanceService.createAttendance(userId, year, month, day, status, remark);
        return Result.success();
    }

    @PutMapping("/update/{id}")
    public Result<Void> updateAttendance(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        String status = (String) request.get("status");
        String remark = (String) request.get("remark");

        attendanceService.updateAttendance(id, status, remark);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.deleteAttendance(id);
        return Result.success();
    }

    @PostMapping("/mark")
    public Result<Void> markAttendance(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Integer year = Integer.valueOf(request.get("year").toString());
        Integer month = Integer.valueOf(request.get("month").toString());
        Integer day = Integer.valueOf(request.get("day").toString());
        String status = (String) request.get("status");
        String remark = request.get("remark") != null ? (String) request.get("remark") : "";

        attendanceService.markAttendance(userId, year, month, day, status, remark);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long userId) {
        Map<String, Object> statistics = attendanceService.getAttendanceStatistics(year, month, userId);
        return Result.success(statistics);
    }

    @GetMapping("/user/{userId}/statistics")
    public Result<Map<String, Object>> getUserStatistics(
            @PathVariable Long userId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        Map<String, Object> statistics = attendanceService.getUserAttendanceStatistics(userId, year, month);
        return Result.success(statistics);
    }

    @GetMapping("/export")
    public void exportAttendance(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long userId,
            HttpServletResponse response) throws IOException {
        List<AttendanceVO> attendances;
        if (userId != null) {
            attendances = attendanceService.getAttendancesByUserId(userId).stream()
                    .filter(a -> a.getYear().equals(year) && a.getMonth().equals(month))
                    .toList();
        } else {
            attendances = attendanceService.getAttendancesByYearMonth(year, month);
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("考勤记录");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        Row headerRow = sheet.createRow(0);
        String[] headers = {"用户", "日期", "考勤状态", "符号", "备注"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        int rowNum = 1;
        for (AttendanceVO attendance : attendances) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(attendance.getUserName() != null ? attendance.getUserName() : "");
            row.createCell(1).setCellValue(attendance.getYear() + "-" + attendance.getMonth() + "-" + attendance.getDay());
            row.createCell(2).setCellValue(attendance.getStatus() != null ? attendance.getStatus() : "");
            row.createCell(3).setCellValue(getStatusSymbol(attendance.getStatus()));
            row.createCell(4).setCellValue(attendance.getRemark() != null ? attendance.getRemark() : "");
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        String fileName = URLEncoder.encode("考勤记录_" + year + "年" + month + "月.xlsx", StandardCharsets.UTF_8);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private String getStatusSymbol(String status) {
        if (status == null) return "";
        if (status.contains("正常上班")) return "●";
        if (status.contains("迟到") || status.contains("早退")) return "⏰";
        if (status.contains("旷工")) return "✗";
        if (status.contains("全天休假") || status.contains("全天请假")) return "○";
        if (status.contains("半天休假") || status.contains("半天请假")) return "◐";
        if (status.contains("加班")) return "■";
        if (status.contains("出差")) return "✈";
        return "";
    }

    @GetMapping("/matrix")
    public Result<List<Map<String, Object>>> getAttendanceMatrix(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        List<Map<String, Object>> matrix = attendanceService.getAttendanceMatrix(year, month);
        return Result.success(matrix);
    }
}