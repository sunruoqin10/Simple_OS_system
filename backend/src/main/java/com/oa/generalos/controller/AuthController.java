package com.oa.generalos.controller;

import com.oa.generalos.common.Result;
import com.oa.generalos.dto.LoginDTO;
import com.oa.generalos.service.UserService;
import com.oa.generalos.vo.LoginVO;
import com.oa.generalos.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO loginDTO) {
        LoginVO loginVO = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return Result.success(loginVO);
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }

    @GetMapping("/current")
    public Result<UserVO> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }
        UserVO userVO = userService.getCurrentUser(userId);
        return Result.success(userVO);
    }

    @PostMapping("/change-password")
    public Result<Void> changePassword(HttpServletRequest request,
                                       @RequestBody Map<String, String> passwordData) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return Result.unauthorized("请先登录");
        }

        String oldPassword = passwordData.get("oldPassword");
        String newPassword = passwordData.get("newPassword");

        if (oldPassword == null || oldPassword.trim().isEmpty()) {
            return Result.error(400, "原密码不能为空");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return Result.error(400, "新密码不能为空");
        }
        if (newPassword.length() < 6) {
            return Result.error(400, "新密码长度不能少于6位");
        }

        userService.updatePassword(userId, oldPassword, newPassword);
        return Result.success();
    }
}
