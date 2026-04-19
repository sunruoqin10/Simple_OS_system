package com.oa.generalos.controller;

import com.oa.generalos.common.Result;
import com.oa.generalos.entity.Holiday;
import com.oa.generalos.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/holiday")
public class HolidayController {

    @Autowired
    private HolidayService holidayService;

    @GetMapping("/list")
    public Result<List<Holiday>> getAllHolidays() {
        return Result.success(holidayService.getAllHolidays());
    }

    @GetMapping("/year/{year}")
    public Result<List<Holiday>> getHolidaysByYear(@PathVariable Integer year) {
        return Result.success(holidayService.getHolidaysByYear(year));
    }

    @GetMapping("/check")
    public Result<Map<String, Object>> checkDate(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Integer day) {
        boolean isHoliday = holidayService.isHoliday(year, month, day);
        boolean isWorkday = holidayService.isWorkday(year, month, day);
        Holiday holiday = holidayService.getHolidayByDate(year, month, day);

        return Result.success(Map.of(
            "isHoliday", isHoliday,
            "isWorkday", isWorkday,
            "holiday", holiday
        ));
    }

    @GetMapping("/{id}")
    public Result<Holiday> getHolidayById(@PathVariable Long id) {
        return Result.success(holidayService.getHolidayById(id));
    }

    @PostMapping("/create")
    public Result<Void> createHoliday(@RequestBody Holiday holiday) {
        holidayService.createHoliday(holiday);
        return Result.success();
    }

    @PutMapping("/update/{id}")
    public Result<Void> updateHoliday(@PathVariable Long id, @RequestBody Holiday holiday) {
        holidayService.updateHoliday(id, holiday);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteHoliday(@PathVariable Long id) {
        holidayService.deleteHoliday(id);
        return Result.success();
    }

    @DeleteMapping("/year/{year}")
    public Result<Void> deleteHolidaysByYear(@PathVariable Integer year) {
        holidayService.deleteHolidaysByYear(year);
        return Result.success();
    }
}
