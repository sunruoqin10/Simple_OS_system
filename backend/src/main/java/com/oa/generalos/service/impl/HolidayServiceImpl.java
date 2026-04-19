package com.oa.generalos.service.impl;

import com.oa.generalos.entity.Holiday;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.mapper.HolidayMapper;
import com.oa.generalos.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolidayMapper holidayMapper;

    @Override
    public List<Holiday> getAllHolidays() {
        return holidayMapper.findAll();
    }

    @Override
    public List<Holiday> getHolidaysByYear(Integer year) {
        return holidayMapper.findByYear(year);
    }

    @Override
    public Holiday getHolidayById(Long id) {
        Holiday holiday = holidayMapper.findById(id);
        if (holiday == null) {
            throw new BusinessException(404, "节假日不存在");
        }
        return holiday;
    }

    @Override
    public Holiday getHolidayByDate(Integer year, Integer month, Integer day) {
        return holidayMapper.findByDate(year, month, day);
    }

    @Override
    public List<Holiday> getHolidayDatesByYear(Integer year) {
        return holidayMapper.findHolidaysByYear(year);
    }

    @Override
    public List<Holiday> getWorkdayDatesByYear(Integer year) {
        return holidayMapper.findWorkdaysByYear(year);
    }

    @Override
    public boolean isHoliday(Integer year, Integer month, Integer day) {
        Holiday holiday = holidayMapper.findByDate(year, month, day);
        if (holiday != null && "法定节假日".equals(holiday.getType())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isWorkday(Integer year, Integer month, Integer day) {
        Holiday holiday = holidayMapper.findByDate(year, month, day);
        if (holiday != null && "调休".equals(holiday.getType())) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void createHoliday(Holiday holiday) {
        if (holiday.getYear() == null || holiday.getMonth() == null || holiday.getDay() == null) {
            throw new BusinessException(400, "日期不能为空");
        }
        if (holiday.getName() == null || holiday.getName().trim().isEmpty()) {
            throw new BusinessException(400, "节假日名称不能为空");
        }
        if (holiday.getType() == null || holiday.getType().trim().isEmpty()) {
            holiday.setType("法定节假日");
        }

        Holiday existing = holidayMapper.findByDate(holiday.getYear(), holiday.getMonth(), holiday.getDay());
        if (existing != null) {
            throw new BusinessException(400, "该日期已存在节假日配置");
        }

        holidayMapper.insert(holiday);
    }

    @Override
    @Transactional
    public void updateHoliday(Long id, Holiday holiday) {
        Holiday existing = holidayMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "节假日不存在");
        }

        if (holiday.getName() == null || holiday.getName().trim().isEmpty()) {
            throw new BusinessException(400, "节假日名称不能为空");
        }
        if (holiday.getType() == null || holiday.getType().trim().isEmpty()) {
            holiday.setType("法定节假日");
        }

        holiday.setId(id);
        holidayMapper.update(holiday);
    }

    @Override
    @Transactional
    public void deleteHoliday(Long id) {
        Holiday existing = holidayMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "节假日不存在");
        }
        holidayMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteHolidaysByYear(Integer year) {
        holidayMapper.deleteByYear(year);
    }
    
    @Override
    @Transactional
    public void batchCreateHolidays(List<Holiday> holidays) {
        if (holidays == null || holidays.isEmpty()) {
            throw new BusinessException(400, "节假日数据不能为空");
        }
        
        for (Holiday holiday : holidays) {
            if (holiday.getYear() == null || holiday.getMonth() == null || holiday.getDay() == null) {
                throw new BusinessException(400, "日期不能为空");
            }
            if (holiday.getName() == null || holiday.getName().trim().isEmpty()) {
                throw new BusinessException(400, "节假日名称不能为空");
            }
            if (holiday.getType() == null || holiday.getType().trim().isEmpty()) {
                holiday.setType("法定节假日");
            }
            
            Holiday existing = holidayMapper.findByDate(holiday.getYear(), holiday.getMonth(), holiday.getDay());
            if (existing != null) {
                throw new BusinessException(400, "日期 " + holiday.getYear() + "-" + 
                    String.format("%02d", holiday.getMonth()) + "-" + 
                    String.format("%02d", holiday.getDay()) + " 已存在节假日配置");
            }
            
            holidayMapper.insert(holiday);
        }
    }
}
