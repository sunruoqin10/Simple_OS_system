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
}
