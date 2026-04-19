package com.oa.generalos.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

public class SecurityContext {
    
    public static Long getCurrentUserId() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        Object userIdObj = request.getAttribute("userId");
        return userIdObj != null ? (Long) userIdObj : null;
    }
    
    public static String getCurrentUsername() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return request.getAttribute("username") != null 
                ? (String) request.getAttribute("username") 
                : null;
    }
    
    @SuppressWarnings("unchecked")
    public static List<String> getCurrentPermissions() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return List.of();
        }
        Object permissionsObj = request.getAttribute("userPermissions");
        if (permissionsObj instanceof List) {
            return (List<String>) permissionsObj;
        }
        return List.of();
    }
    
    public static boolean hasPermission(String permission) {
        List<String> permissions = getCurrentPermissions();
        return permissions.contains(permission);
    }
    
    public static boolean hasAnyPermission(String... perms) {
        for (String perm : perms) {
            if (hasPermission(perm)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasAllPermissions(String... perms) {
        for (String perm : perms) {
            if (!hasPermission(perm)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isCurrentUser(Long userId) {
        Long currentUserId = getCurrentUserId();
        return currentUserId != null && currentUserId.equals(userId);
    }
    
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }
    
    public static boolean isAuthenticated() {
        return getCurrentUserId() != null;
    }
}
