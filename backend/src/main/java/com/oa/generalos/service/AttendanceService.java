package com.oa.generalos.service;

import com.oa.generalos.vo.AttendanceVO;
import java.util.List;
import java.util.Map;

public interface AttendanceService {
    List<AttendanceVO> getAllAttendances();

    List<AttendanceVO> getAttendancesByUserId(Long userId);

    List<AttendanceVO> getAttendancesByYearMonth(Integer year, Integer month);

    AttendanceVO getAttendanceById(Long id);

    void createAttendance(Long userId, Integer year, Integer month, Integer day, String status, String remark);

    void updateAttendance(Long id, String status, String remark);

    void deleteAttendance(Long id);

    void markAttendance(Long userId, Integer year, Integer month, Integer day, String status, String remark);

    Map<String, Object> getAttendanceStatistics(Integer year, Integer month);

    Map<String, Object> getUserAttendanceStatistics(Long userId, Integer year, Integer month);
}
