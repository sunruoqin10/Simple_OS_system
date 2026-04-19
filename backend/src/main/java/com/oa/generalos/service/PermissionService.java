package com.oa.generalos.service;

import com.oa.generalos.entity.Permission;
import com.oa.generalos.vo.PermissionVO;
import java.util.List;

public interface PermissionService {
    List<PermissionVO> getAllPermissions();

    List<PermissionVO> getPermissionsByStatus(Integer status);

    List<PermissionVO> getPermissionsByCategory(String category);

    PermissionVO getPermissionById(Long id);

    void createPermission(Permission permission);

    void updatePermission(Permission permission);

    void deletePermission(Long id);

    List<PermissionVO> getPermissionsByRoleId(Long roleId);

    List<String> getPermissionCodesByRoleId(Long roleId);

    void assignPermissionsToRole(Long roleId, List<Long> permissionIds);

    void removePermissionsFromRole(Long roleId, List<Long> permissionIds);

    int getPermissionCountByRoleId(Long roleId);
}