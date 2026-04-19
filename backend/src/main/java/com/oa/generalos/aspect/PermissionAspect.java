package com.oa.generalos.aspect;

import com.oa.generalos.annotation.CurrentUser;
import com.oa.generalos.annotation.OwnerOnly;
import com.oa.generalos.annotation.RequirePermission;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.security.SecurityContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
@Order(1)
public class PermissionAspect {

    @Before("@annotation(requirePermission)")
    public void checkPermission(JoinPoint joinPoint, RequirePermission requirePermission) {
        Long userId = SecurityContext.getCurrentUserId();
        if (userId == null) {
            throw new BusinessException(401, "用户未登录");
        }

        String[] requiredPermissions = requirePermission.value();
        boolean hasPermission;

        if (requirePermission.logical() == RequirePermission.Logical.AND) {
            hasPermission = SecurityContext.hasAllPermissions(requiredPermissions);
        } else {
            hasPermission = SecurityContext.hasAnyPermission(requiredPermissions);
        }

        if (!hasPermission) {
            throw new BusinessException(403, "权限不足");
        }
    }

    @Before("@annotation(ownerOnly)")
    public void checkOwner(JoinPoint joinPoint, OwnerOnly ownerOnly) throws Throwable {
        if (!SecurityContext.hasPermission(ownerOnly.entityClass() + "_" + ownerOnly.ownerField())) {
            return;
        }
        
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        
        Long currentUserId = SecurityContext.getCurrentUserId();
        if (currentUserId == null) {
            throw new BusinessException(401, "用户未登录");
        }
        
        Long ownerId = null;
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(CurrentUser.class)) {
                ownerId = (Long) args[i];
                break;
            }
        }
        
        if (ownerId == null) {
            ownerId = SecurityContext.getCurrentUserId();
        }
        
        if (!currentUserId.equals(ownerId)) {
            throw new BusinessException(403, "只能操作自己的数据");
        }
    }
}
