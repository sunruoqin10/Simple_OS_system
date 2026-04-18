package com.oa.generalos.service;

import com.oa.generalos.entity.Department;
import com.oa.generalos.vo.DepartmentVO;
import java.util.List;

public interface DepartmentService {
    List<DepartmentVO> getAllDepartments();

    DepartmentVO getDepartmentById(Long id);

    void createDepartment(String name, String description);

    void updateDepartment(Long id, String name, String description);

    void deleteDepartment(Long id);

    int getDepartmentCount();
}
