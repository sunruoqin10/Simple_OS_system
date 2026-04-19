package com.oa.generalos.service.impl;

import com.oa.generalos.entity.Permission;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.mapper.PermissionMapper;
import com.oa.generalos.mapper.RoleMapper;
import com.oa.generalos.mapper.RolePermissionMapper;
import com.oa.generalos.service.PermissionService;
import com.oa.generalos.vo.PermissionVO;
import com.oa.generalos.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<PermissionVO> getAllPermissions() {
        return permissionMapper.findAll();
    }

    @Override
    public List<PermissionVO> getPermissionsByStatus(Integer status) {
        return permissionMapper.findByStatus(status);
    }

    @Override
    public List<PermissionVO> getPermissionsByCategory(String category) {
        return permissionMapper.findByCategory(category);
    }

    @Override
    public PermissionVO getPermissionById(Long id) {
        PermissionVO permission = permissionMapper.findById(id);
        if (permission == null) {
            throw new BusinessException(404, "权限不存在");
        }
        return permission;
    }

    @Override
    @Transactional
    public void createPermission(Permission permission) {
        PermissionVO existing = permissionMapper.findByCode(permission.getCode());
        if (existing != null) {
            throw new BusinessException(400, "权限代码已存在");
        }
        permissionMapper.insert(permission);
    }

    @Override
    @Transactional
    public void updatePermission(Permission permission) {
        PermissionVO existing = permissionMapper.findById(permission.getId());
        if (existing == null) {
            throw new BusinessException(404, "权限不存在");
        }
        permissionMapper.update(permission);
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        PermissionVO existing = permissionMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "权限不存在");
        }
        permissionMapper.deleteById(id);
    }

    @Override
    public List<PermissionVO> getPermissionsByRoleId(Long roleId) {
        return permissionMapper.findByRoleId(roleId);
    }

    @Override
    public List<String> getPermissionCodesByRoleId(Long roleId) {
        return permissionMapper.findCodesByRoleId(roleId);
    }

    @Override
    @Transactional
    public void assignPermissionsToRole(Long roleId, List<Long> permissionIds) {
        RoleVO role = roleMapper.findById(roleId);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }
        for (Long permissionId : permissionIds) {
            PermissionVO permission = permissionMapper.findById(permissionId);
            if (permission == null) {
                throw new BusinessException(404, "权限不存在: " + permissionId);
            }
            rolePermissionMapper.insert(roleId, permissionId);
        }
    }

    @Override
    @Transactional
    public void removePermissionsFromRole(Long roleId, List<Long> permissionIds) {
        for (Long permissionId : permissionIds) {
            rolePermissionMapper.deleteByRoleIdAndPermissionId(roleId, permissionId);
        }
    }

    @Override
    public int getPermissionCountByRoleId(Long roleId) {
        return permissionMapper.countByRoleId(roleId);
    }
}