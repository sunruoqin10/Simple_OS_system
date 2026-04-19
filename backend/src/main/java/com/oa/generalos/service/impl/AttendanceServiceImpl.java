package com.oa.generalos.service.impl;

import com.oa.generalos.entity.Attendance;
import com.oa.generalos.entity.User;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.mapper.AttendanceMapper;
import com.oa.generalos.mapper.UserMapper;
import com.oa.generalos.service.AttendanceService;
import com.oa.generalos.vo.AttendanceVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<AttendanceVO> getAllAttendances() {
        List<Attendance> attendances = attendanceMapper.findAll();
        return attendances.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceVO> getAttendancesByUserId(Long userId) {
        List<Attendance> attendances = attendanceMapper.findByUserId(userId);
        return attendances.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AttendanceVO> getAttendancesByYearMonth(Integer year, Integer month) {
        List<Attendance> attendances = attendanceMapper.findByYearMonth(year, month);
        return attendances.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public AttendanceVO getAttendanceById(Long id) {
        Attendance attendance = attendanceMapper.findById(id);
        if (attendance == null) {
            throw new BusinessException(404, "考勤记录不存在");
        }
        return convertToVO(attendance);
    }

    @Override
    public void createAttendance(Long userId, Integer year, Integer month, Integer day, String status, String remark) {
        Attendance existing = attendanceMapper.findByUserIdAndDate(userId, year, month, day);
        if (existing != null) {
            throw new BusinessException(400, "该日期的考勤记录已存在");
        }

        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setYear(year);
        attendance.setMonth(month);
        attendance.setDay(day);
        attendance.setStatus(status);
        attendance.setRemark(remark);

        attendanceMapper.insert(attendance);
    }

    @Override
    public void updateAttendance(Long id, String status, String remark) {
        Attendance existing = attendanceMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "考勤记录不存在");
        }

        existing.setStatus(status);
        existing.setRemark(remark);
        attendanceMapper.update(existing);
    }

    @Override
    public void deleteAttendance(Long id) {
        Attendance existing = attendanceMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "考勤记录不存在");
        }
        attendanceMapper.deleteById(id);
    }

    @Override
    public void markAttendance(Long userId, Integer year, Integer month, Integer day, String status, String remark) {
        Attendance existing = attendanceMapper.findByUserIdAndDate(userId, year, month, day);
        if (existing != null) {
            existing.setStatus(status);
            existing.setRemark(remark);
            attendanceMapper.update(existing);
        } else {
            Attendance attendance = new Attendance();
            attendance.setUserId(userId);
            attendance.setYear(year);
            attendance.setMonth(month);
            attendance.setDay(day);
            attendance.setStatus(status);
            attendance.setRemark(remark);
            attendanceMapper.insert(attendance);
        }
    }

    @Override
    public Map<String, Object> getAttendanceStatistics(Integer year, Integer month) {
        return getAttendanceStatistics(year, month, null);
    }

    @Override
    public Map<String, Object> getAttendanceStatistics(Integer year, Integer month, Long userId) {
        List<Attendance> attendances;
        if (userId != null) {
            attendances = attendanceMapper.findByUserId(userId).stream()
                    .filter(a -> a.getYear().equals(year) && a.getMonth().equals(month))
                    .collect(Collectors.toList());
        } else {
            attendances = attendanceMapper.findByYearMonth(year, month);
        }

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("year", year);
        statistics.put("month", month);

        int normal = 0;
        int abnormal = 0;
        int leave = 0;
        int other = 0;

        for (Attendance attendance : attendances) {
            String status = attendance.getStatus();
            if (status != null) {
                if (status.contains("正常")) {
                    normal++;
                } else if (status.contains("迟到") || status.contains("早退") || status.contains("旷工")) {
                    abnormal++;
                } else if (status.contains("假") || status.contains("休假")) {
                    leave++;
                } else {
                    other++;
                }
            }
        }

        statistics.put("total", attendances.size());
        statistics.put("normal", normal);
        statistics.put("abnormal", abnormal);
        statistics.put("leave", leave);
        statistics.put("other", other);

        return statistics;
    }

    @Override
    public Map<String, Object> getUserAttendanceStatistics(Long userId, Integer year, Integer month) {
        return getAttendanceStatistics(year, month, userId);
    }

    @Override
    public List<Map<String, Object>> getAttendanceMatrix(Integer year, Integer month) {
        List<User> allUsers = userMapper.findAll();
        List<Attendance> allAttendances = attendanceMapper.findByYearMonth(year, month);

        List<Map<String, Object>> matrix = new java.util.ArrayList<>();

        for (User user : allUsers) {
            Map<String, Object> row = new java.util.HashMap<>();
            row.put("userId", user.getId());
            row.put("userName", user.getRealName() != null ? user.getRealName() : user.getUsername());

            Map<Integer, String> dayStatus = new java.util.HashMap<>();
            for (Attendance att : allAttendances) {
                if (att.getUserId().equals(user.getId())) {
                    dayStatus.put(att.getDay(), att.getStatus());
                }
            }
            row.put("days", dayStatus);
            matrix.add(row);
        }

        return matrix;
    }

    private AttendanceVO convertToVO(Attendance attendance) {
        AttendanceVO vo = new AttendanceVO();
        BeanUtils.copyProperties(attendance, vo);

        var user = userMapper.findById(attendance.getUserId());
        if (user != null) {
            vo.setUserName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        }

        return vo;
    }
}