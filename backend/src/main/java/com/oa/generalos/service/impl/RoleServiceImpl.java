package com.oa.generalos.service.impl;

import com.oa.generalos.entity.Role;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.mapper.PermissionMapper;
import com.oa.generalos.mapper.RoleMapper;
import com.oa.generalos.mapper.UserRoleMapper;
import com.oa.generalos.service.RoleService;
import com.oa.generalos.vo.RoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<RoleVO> getAllRoles() {
        List<RoleVO> roles = roleMapper.findAll();
        for (RoleVO role : roles) {
            List<String> permissions = permissionMapper.findCodesByRoleId(role.getId());
            role.setPermissions(permissions);
        }
        return roles;
    }

    @Override
    public List<RoleVO> getRolesByStatus(Integer status) {
        List<RoleVO> roles = roleMapper.findByStatus(status);
        for (RoleVO role : roles) {
            List<String> permissions = permissionMapper.findCodesByRoleId(role.getId());
            role.setPermissions(permissions);
        }
        return roles;
    }

    @Override
    public RoleVO getRoleById(Long id) {
        RoleVO role = roleMapper.findById(id);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }
        List<String> permissions = permissionMapper.findCodesByRoleId(role.getId());
        role.setPermissions(permissions);
        return role;
    }

    @Override
    public RoleVO getRoleByCode(String code) {
        RoleVO role = roleMapper.findByCode(code);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }
        List<String> permissions = permissionMapper.findCodesByRoleId(role.getId());
        role.setPermissions(permissions);
        return role;
    }

    @Override
    @Transactional
    public void createRole(Role role) {
        if (roleMapper.countByCode(role.getCode()) > 0) {
            throw new BusinessException(400, "角色代码已存在");
        }
        roleMapper.insert(role);
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        RoleVO existing = roleMapper.findById(role.getId());
        if (existing == null) {
            throw new BusinessException(404, "角色不存在");
        }
        roleMapper.update(role);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        RoleVO existing = roleMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "角色不存在");
        }
        if (roleMapper.countUserByRoleId(id) > 0) {
            throw new BusinessException(400, "该角色下有用户，无法删除");
        }
        roleMapper.deleteById(id);
    }

    @Override
    public List<String> getPermissionCodesByUserId(Long userId) {
        List<String> permissions = userRoleMapper.findPermissionCodesByUserId(userId);
        return permissions != null ? permissions : new ArrayList<>();
    }

    @Override
    @Transactional
    public void assignRoleToUser(Long userId, Long roleId) {
        RoleVO role = roleMapper.findById(roleId);
        if (role == null) {
            throw new BusinessException(404, "角色不存在");
        }
        userRoleMapper.insert(userId, roleId);
    }

    @Override
    @Transactional
    public void removeRoleFromUser(Long userId, Long roleId) {
        userRoleMapper.deleteByUserIdAndRoleId(userId, roleId);
    }

    @Override
    @Transactional
    public void setUserRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.deleteByUserId(userId);
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                RoleVO role = roleMapper.findById(roleId);
                if (role != null) {
                    userRoleMapper.insert(userId, roleId);
                }
            }
        }
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        List<Long> roleIds = userRoleMapper.findRoleIdsByUserId(userId);
        return roleIds != null ? roleIds : new ArrayList<>();
    }

    @Override
    public List<String> getPermissionCodesByRoleId(Long roleId) {
        List<String> permissions = permissionMapper.findCodesByRoleId(roleId);
        return permissions != null ? permissions : new ArrayList<>();
    }
}