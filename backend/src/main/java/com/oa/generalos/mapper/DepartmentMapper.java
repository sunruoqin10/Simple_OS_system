package com.oa.generalos.mapper;

import com.oa.generalos.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DepartmentMapper {
    List<Department> findAll();

    Department findById(@Param("id") Long id);

    Department findByName(@Param("name") String name);

    int insert(Department department);

    int update(Department department);

    int deleteById(@Param("id") Long id);

    int count();
}
