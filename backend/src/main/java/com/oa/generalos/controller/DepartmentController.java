package com.oa.generalos.controller;

import com.oa.generalos.common.Result;
import com.oa.generalos.service.DepartmentService;
import com.oa.generalos.vo.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/list")
    public Result<List<DepartmentVO>> getAllDepartments() {
        List<DepartmentVO> departments = departmentService.getAllDepartments();
        return Result.success(departments);
    }

    @GetMapping("/{id}")
    public Result<DepartmentVO> getDepartmentById(@PathVariable Long id) {
        DepartmentVO department = departmentService.getDepartmentById(id);
        return Result.success(department);
    }

    @PostMapping("/create")
    public Result<Void> createDepartment(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");
        departmentService.createDepartment(name, description);
        return Result.success();
    }

    @PutMapping("/update/{id}")
    public Result<Void> updateDepartment(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String name = request.get("name");
        String description = request.get("description");
        departmentService.updateDepartment(id, name, description);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return Result.success();
    }

    @GetMapping("/count")
    public Result<Integer> getDepartmentCount() {
        int count = departmentService.getDepartmentCount();
        return Result.success(count);
    }
}
