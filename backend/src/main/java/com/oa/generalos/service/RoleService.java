package com.oa.generalos.service;

import com.oa.generalos.entity.Role;
import com.oa.generalos.vo.RoleVO;
import java.util.List;

public interface RoleService {
    List<RoleVO> getAllRoles();

    List<RoleVO> getRolesByStatus(Integer status);

    RoleVO getRoleById(Long id);

    RoleVO getRoleByCode(String code);

    void createRole(Role role);

    void updateRole(Role role);

    void deleteRole(Long id);

    List<String> getPermissionCodesByUserId(Long userId);

    void assignRoleToUser(Long userId, Long roleId);

    void removeRoleFromUser(Long userId, Long roleId);

    void setUserRoles(Long userId, List<Long> roleIds);

    List<Long> getUserRoleIds(Long userId);

    List<String> getPermissionCodesByRoleId(Long roleId);
}