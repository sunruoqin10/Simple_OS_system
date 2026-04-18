package com.oa.generalos.service.impl;

import com.oa.generalos.entity.Department;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.mapper.DepartmentMapper;
import com.oa.generalos.service.DepartmentService;
import com.oa.generalos.vo.DepartmentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentVO> getAllDepartments() {
        List<Department> departments = departmentMapper.findAll();
        return departments.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentVO getDepartmentById(Long id) {
        Department department = departmentMapper.findById(id);
        if (department == null) {
            throw new BusinessException(404, "部门不存在");
        }
        return convertToVO(department);
    }

    @Override
    public void createDepartment(String name, String description) {
        Department existing = departmentMapper.findByName(name);
        if (existing != null) {
            throw new BusinessException(400, "部门名称已存在");
        }

        Department department = new Department();
        department.setName(name);
        department.setDescription(description);
        departmentMapper.insert(department);
    }

    @Override
    public void updateDepartment(Long id, String name, String description) {
        Department existing = departmentMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "部门不存在");
        }

        Department byName = departmentMapper.findByName(name);
        if (byName != null && !byName.getId().equals(id)) {
            throw new BusinessException(400, "部门名称已存在");
        }

        existing.setName(name);
        existing.setDescription(description);
        departmentMapper.update(existing);
    }

    @Override
    public void deleteDepartment(Long id) {
        Department existing = departmentMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "部门不存在");
        }
        departmentMapper.deleteById(id);
    }

    @Override
    public int getDepartmentCount() {
        return departmentMapper.count();
    }

    private DepartmentVO convertToVO(Department department) {
        DepartmentVO vo = new DepartmentVO();
        BeanUtils.copyProperties(department, vo);
        return vo;
    }
}
