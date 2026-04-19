package com.oa.generalos.service;

import com.oa.generalos.entity.Holiday;
import java.util.List;

public interface HolidayService {
    List<Holiday> getAllHolidays();

    List<Holiday> getHolidaysByYear(Integer year);

    Holiday getHolidayById(Long id);

    Holiday getHolidayByDate(Integer year, Integer month, Integer day);

    List<Holiday> getHolidayDatesByYear(Integer year);

    List<Holiday> getWorkdayDatesByYear(Integer year);

    boolean isHoliday(Integer year, Integer month, Integer day);

    boolean isWorkday(Integer year, Integer month, Integer day);

    void createHoliday(Holiday holiday);

    void updateHoliday(Long id, Holiday holiday);

    void deleteHoliday(Long id);

    void deleteHolidaysByYear(Integer year);
}
