package com.oa.generalos.mapper;

import com.oa.generalos.entity.Holiday;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HolidayMapper {
    List<Holiday> findAll();

    Holiday findById(@Param("id") Long id);

    List<Holiday> findByYear(@Param("year") Integer year);

    Holiday findByDate(@Param("year") Integer year, @Param("month") Integer month, @Param("day") Integer day);

    List<Holiday> findByYearAndType(@Param("year") Integer year, @Param("type") String type);

    List<Holiday> findHolidaysByYear(@Param("year") Integer year);

    List<Holiday> findWorkdaysByYear(@Param("year") Integer year);

    int insert(Holiday holiday);

    int update(Holiday holiday);

    int deleteById(@Param("id") Long id);

    int deleteByYear(@Param("year") Integer year);

    int count();
}
