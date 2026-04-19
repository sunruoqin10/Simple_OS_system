package com.oa.generalos.controller;

import com.oa.generalos.annotation.CurrentUser;
import com.oa.generalos.annotation.RequirePermission;
import com.oa.generalos.common.Result;
import com.oa.generalos.security.SecurityContext;
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
    @RequirePermission("ATT_VIEW_ALL")
    public Result<List<AttendanceVO>> getAllAttendances() {
        return Result.success(attendanceService.getAllAttendances());
    }

    @GetMapping("/user/{userId}")
    public Result<List<AttendanceVO>> getAttendancesByUserId(
            @PathVariable Long userId,
            @CurrentUser Long currentUserId) {
        if (!SecurityContext.isCurrentUser(userId) && !SecurityContext.hasPermission("ATT_VIEW_ALL")) {
            return Result.error(403, "权限不足，只能查看自己的考勤记录");
        }
        return Result.success(attendanceService.getAttendancesByUserId(userId));
    }

    @GetMapping("/calendar")
    public Result<List<AttendanceVO>> getAttendancesByYearMonth(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long userId,
            @CurrentUser Long currentUserId) {
        List<AttendanceVO> attendances;
        
        if (userId == null) {
            userId = SecurityContext.hasPermission("ATT_VIEW_ALL") ? null : currentUserId;
        } else if (!SecurityContext.isCurrentUser(userId) && !SecurityContext.hasPermission("ATT_VIEW_ALL")) {
            return Result.error(403, "权限不足");
        }
        
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
        if (SecurityContext.isCurrentUser(attendance.getUserId()) || SecurityContext.hasPermission("ATT_VIEW_ALL")) {
            return Result.success(attendance);
        }
        return Result.error(403, "权限不足");
    }

    @PostMapping("/create")
    public Result<Void> createAttendance(
            @RequestBody Map<String, Object> request,
            @CurrentUser Long currentUserId) {
        Long userId = Long.valueOf(request.get("userId").toString());
        if (!SecurityContext.isCurrentUser(userId) && !SecurityContext.hasPermission("ATT_VIEW_ALL")) {
            return Result.error(403, "权限不足，只能为自己创建考勤记录");
        }
        
        attendanceService.createAttendance(
                userId,
                Integer.valueOf(request.get("year").toString()),
                Integer.valueOf(request.get("month").toString()),
                Integer.valueOf(request.get("day").toString()),
                (String) request.get("status"),
                (String) request.get("remark"));
        return Result.success();
    }

    @PutMapping("/update/{id}")
    @RequirePermission(value = {"ATT_EDIT_OWN", "ATT_VIEW_ALL"}, logical = RequirePermission.Logical.OR)
    public Result<Void> updateAttendance(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {
        AttendanceVO attendance = attendanceService.getAttendanceById(id);
        if (!SecurityContext.isCurrentUser(attendance.getUserId()) && !SecurityContext.hasPermission("ATT_VIEW_ALL")) {
            return Result.error(403, "权限不足，只能修改自己的考勤记录");
        }

        attendanceService.updateAttendance(id, 
                (String) request.get("status"), 
                (String) request.get("remark"));
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    @RequirePermission(value = {"ATT_EDIT_OWN", "ATT_VIEW_ALL"}, logical = RequirePermission.Logical.OR)
    public Result<Void> deleteAttendance(@PathVariable Long id) {
        AttendanceVO attendance = attendanceService.getAttendanceById(id);
        if (!SecurityContext.isCurrentUser(attendance.getUserId()) && !SecurityContext.hasPermission("ATT_VIEW_ALL")) {
            return Result.error(403, "权限不足，只能删除自己的考勤记录");
        }
        attendanceService.deleteAttendance(id);
        return Result.success();
    }

    @PostMapping("/mark")
    @RequirePermission(value = {"ATT_EDIT_OWN", "ATT_VIEW_ALL"}, logical = RequirePermission.Logical.OR)
    public Result<Void> markAttendance(
            @RequestBody Map<String, Object> request,
            @CurrentUser Long currentUserId) {
        Long userId = Long.valueOf(request.get("userId").toString());
        if (!SecurityContext.isCurrentUser(userId) && !SecurityContext.hasPermission("ATT_VIEW_ALL")) {
            return Result.error(403, "权限不足，只能为自己打卡");
        }
        
        attendanceService.markAttendance(
                userId,
                Integer.valueOf(request.get("year").toString()),
                Integer.valueOf(request.get("month").toString()),
                Integer.valueOf(request.get("day").toString()),
                (String) request.get("status"),
                request.get("remark") != null ? (String) request.get("remark") : "");
        return Result.success();
    }

    @GetMapping("/statistics")
    @RequirePermission(value = {"ATT_VIEW_ALL", "ATT_VIEW_OWN"}, logical = RequirePermission.Logical.OR)
    public Result<Map<String, Object>> getStatistics(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long userId) {
        if (userId != null && !SecurityContext.isCurrentUser(userId) && !SecurityContext.hasPermission("ATT_VIEW_ALL")) {
            return Result.error(403, "权限不足");
        }
        Map<String, Object> statistics = attendanceService.getAttendanceStatistics(year, month, userId);
        return Result.success(statistics);
    }

    @GetMapping("/user/{userId}/statistics")
    @RequirePermission(value = {"ATT_VIEW_OWN", "ATT_VIEW_ALL"}, logical = RequirePermission.Logical.OR)
    public Result<Map<String, Object>> getUserStatistics(
            @PathVariable Long userId,
            @RequestParam Integer year,
            @RequestParam Integer month) {
        if (!SecurityContext.isCurrentUser(userId) && !SecurityContext.hasPermission("ATT_VIEW_ALL")) {
            return Result.error(403, "权限不足");
        }
        return Result.success(attendanceService.getUserAttendanceStatistics(userId, year, month));
    }

    @GetMapping("/export")
    @RequirePermission("ATT_EXPORT")
    public void exportAttendance(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long userId,
            HttpServletResponse response) throws IOException {
        List<AttendanceVO> attendances;
        
        if (userId != null) {
            if (!SecurityContext.isCurrentUser(userId) && !SecurityContext.hasPermission("ATT_VIEW_ALL")) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"message\":\"权限不足\"}");
                return;
            }
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

    @GetMapping("/matrix")
    @RequirePermission("ATT_VIEW_ALL")
    public Result<List<Map<String, Object>>> getAttendanceMatrix(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        return Result.success(attendanceService.getAttendanceMatrix(year, month));
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
}
