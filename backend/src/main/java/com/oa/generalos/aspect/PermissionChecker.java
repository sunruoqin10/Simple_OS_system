package com.oa.generalos.aspect;

import com.oa.generalos.annotation.RequirePermission;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.security.SecurityContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionChecker {

    public void checkPermissions(RequirePermission requirePermission) {
        Long userId = SecurityContext.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        
        List<String> userPermissions = SecurityContext.getCurrentPermissions();
        String[] requiredPermissions = requirePermission.value();
        RequirePermission.Logical logical = requirePermission.logical();
        
        boolean hasPermission;
        if (logical == RequirePermission.Logical.AND) {
            hasPermission = SecurityContext.hasAllPermissions(requiredPermissions);
        } else {
            hasPermission = SecurityContext.hasAnyPermission(requiredPermissions);
        }
        
        if (!hasPermission) {
            throw new BusinessException(403, "权限不足");
        }
    }

    public boolean hasPermission(String permissionCode) {
        return SecurityContext.hasPermission(permissionCode);
    }

    public boolean hasAnyPermission(String... permissionCodes) {
        return SecurityContext.hasAnyPermission(permissionCodes);
    }
}
