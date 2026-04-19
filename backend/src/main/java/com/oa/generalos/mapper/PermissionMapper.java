package com.oa.generalos.mapper;

import com.oa.generalos.entity.Permission;
import com.oa.generalos.vo.PermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PermissionMapper {
    List<PermissionVO> findAll();

    List<PermissionVO> findByStatus(@Param("status") Integer status);

    List<PermissionVO> findByCategory(@Param("category") String category);

    PermissionVO findById(@Param("id") Long id);

    PermissionVO findByCode(@Param("code") String code);

    int insert(Permission permission);

    int update(Permission permission);

    int deleteById(@Param("id") Long id);

    List<PermissionVO> findByRoleId(@Param("roleId") Long roleId);

    List<String> findCodesByRoleId(@Param("roleId") Long roleId);

    int countByRoleId(@Param("roleId") Long roleId);
}