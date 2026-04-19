package com.oa.generalos.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RolePermissionMapper {
    void insert(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    void deleteByRoleId(@Param("roleId") Long roleId);

    void deleteByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    int countByRoleId(@Param("roleId") Long roleId);
}