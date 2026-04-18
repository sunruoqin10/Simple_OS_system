package com.oa.generalos.controller;

import com.oa.generalos.common.Result;
import com.oa.generalos.service.AttendanceService;
import com.oa.generalos.vo.AttendanceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam Integer month) {
        List<AttendanceVO> attendances = attendanceService.getAttendancesByYearMonth(year, month);
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
        String remark = (String) request.get("remark");

        attendanceService.markAttendance(userId, year, month, day, status, remark);
        return Result.success();
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(
            @RequestParam Integer year,
            @RequestParam Integer month) {
        Map<String, Object> statistics = attendanceService.getAttendanceStatistics(year, month);
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
}
