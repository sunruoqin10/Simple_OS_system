package com.oa.generalos.security;

import com.oa.generalos.service.RoleService;
import com.oa.generalos.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PermissionInterceptor extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RoleService roleService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Long userId = jwtUtils.getUserIdFromToken(token);
                if (userId != null) {
                    List<String> permissions = roleService.getPermissionCodesByUserId(userId);
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("permissions", permissions);
                    userInfo.put("userId", userId);
                    request.setAttribute("userPermissions", permissions);
                    request.setAttribute("userId", userId);
                }
            } catch (Exception e) {
            }
        }

        filterChain.doFilter(request, response);
    }
}