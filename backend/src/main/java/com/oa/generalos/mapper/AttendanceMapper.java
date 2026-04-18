package com.oa.generalos.mapper;

import com.oa.generalos.entity.Attendance;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AttendanceMapper {
    List<Attendance> findAll();

    Attendance findById(@Param("id") Long id);

    List<Attendance> findByUserId(@Param("userId") Long userId);

    List<Attendance> findByYearMonth(@Param("year") Integer year, @Param("month") Integer month);

    Attendance findByUserIdAndDate(@Param("userId") Long userId, @Param("year") Integer year,
                                    @Param("month") Integer month, @Param("day") Integer day);

    int insert(Attendance attendance);

    int update(Attendance attendance);

    int deleteById(@Param("id") Long id);

    int deleteByUserIdAndDate(@Param("userId") Long userId, @Param("year") Integer year,
                              @Param("month") Integer month, @Param("day") Integer day);

    int countByYearMonth(@Param("year") Integer year, @Param("month") Integer month);

    int countByUserIdAndYearMonth(@Param("userId") Long userId, @Param("year") Integer year,
                                  @Param("month") Integer month);
}
