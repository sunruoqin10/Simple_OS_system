package com.oa.generalos.controller;

import com.oa.generalos.common.Result;
import com.oa.generalos.entity.Permission;
import com.oa.generalos.entity.Role;
import com.oa.generalos.service.PermissionService;
import com.oa.generalos.service.RoleService;
import com.oa.generalos.vo.PermissionVO;
import com.oa.generalos.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/role/list")
    public Result<List<RoleVO>> getAllRoles() {
        List<RoleVO> roles = roleService.getAllRoles();
        return Result.success(roles);
    }

    @GetMapping("/role/{id}")
    public Result<RoleVO> getRoleById(@PathVariable Long id) {
        RoleVO role = roleService.getRoleById(id);
        return Result.success(role);
    }

    @PostMapping("/role/create")
    public Result<Void> createRole(@RequestBody Role role) {
        roleService.createRole(role);
        return Result.success(null);
    }

    @PutMapping("/role/update")
    public Result<Void> updateRole(@RequestBody Role role) {
        roleService.updateRole(role);
        return Result.success(null);
    }

    @DeleteMapping("/role/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success(null);
    }

    @GetMapping("/role/{id}/permissions")
    public Result<List<PermissionVO>> getRolePermissions(@PathVariable Long id) {
        List<PermissionVO> permissions = permissionService.getPermissionsByRoleId(id);
        return Result.success(permissions);
    }

    @GetMapping("/role/{id}/permission-codes")
    public Result<List<String>> getRolePermissionCodes(@PathVariable Long id) {
        List<String> codes = permissionService.getPermissionCodesByRoleId(id);
        return Result.success(codes);
    }

    @PutMapping("/role/{id}/permissions")
    public Result<Void> assignPermissions(
            @PathVariable Long id,
            @RequestBody Map<String, List<Long>> request) {
        List<Long> permissionIds = request.get("permissionIds");
        if (permissionIds != null && !permissionIds.isEmpty()) {
            permissionService.assignPermissionsToRole(id, permissionIds);
        }
        return Result.success(null);
    }

    @GetMapping("/permission/list")
    public Result<List<PermissionVO>> getAllPermissions() {
        List<PermissionVO> permissions = permissionService.getAllPermissions();
        return Result.success(permissions);
    }

    @GetMapping("/permission/categories")
    public Result<List<String>> getPermissionCategories() {
        List<PermissionVO> permissions = permissionService.getAllPermissions();
        List<String> categories = permissions.stream()
                .map(PermissionVO::getCategory)
                .distinct()
                .toList();
        return Result.success(categories);
    }

    @GetMapping("/permission/category/{category}")
    public Result<List<PermissionVO>> getPermissionsByCategory(@PathVariable String category) {
        List<PermissionVO> permissions = permissionService.getPermissionsByCategory(category);
        return Result.success(permissions);
    }

    @GetMapping("/user/{userId}/permissions")
    public Result<List<String>> getUserPermissions(@PathVariable Long userId) {
        List<String> permissions = roleService.getPermissionCodesByUserId(userId);
        return Result.success(permissions);
    }

    @GetMapping("/user/{userId}/roles")
    public Result<List<Long>> getUserRoleIds(@PathVariable Long userId) {
        List<Long> roleIds = roleService.getUserRoleIds(userId);
        return Result.success(roleIds);
    }

    @PutMapping("/user/{userId}/roles")
    public Result<Void> setUserRoles(
            @PathVariable Long userId,
            @RequestBody Map<String, List<Long>> request) {
        List<Long> roleIds = request.get("roleIds");
        roleService.setUserRoles(userId, roleIds);
        return Result.success(null);
    }
}