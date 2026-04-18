package com.oa.generalos.controller;

import com.oa.generalos.common.Result;
import com.oa.generalos.service.UserService;
import com.oa.generalos.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result<List<UserVO>> getAllUsers() {
        List<UserVO> users = userService.getAllUsers();
        return Result.success(users);
    }

    @GetMapping("/{id}")
    public Result<UserVO> getUserById(@PathVariable Long id) {
        UserVO user = userService.getCurrentUser(id);
        return Result.success(user);
    }

    @PostMapping("/create")
    public Result<Void> createUser(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        String password = (String) request.get("password");
        String realName = (String) request.get("realName");
        String role = (String) request.get("role");
        Long departmentId = request.get("departmentId") != null ?
                Long.valueOf(request.get("departmentId").toString()) : null;
        String email = (String) request.get("email");
        String phone = (String) request.get("phone");

        userService.createUser(username, password, realName, role, departmentId, email, phone);
        return Result.success();
    }

    @PutMapping("/update/{id}")
    public Result<Void> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        String realName = (String) request.get("realName");
        String role = (String) request.get("role");
        Long departmentId = request.get("departmentId") != null ?
                Long.valueOf(request.get("departmentId").toString()) : null;
        String email = (String) request.get("email");
        String phone = (String) request.get("phone");
        Integer status = request.get("status") != null ?
                Integer.valueOf(request.get("status").toString()) : 1;

        userService.updateUser(id, realName, role, departmentId, email, phone, status);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @GetMapping("/count")
    public Result<Integer> getUserCount() {
        int count = userService.getUserCount();
        return Result.success(count);
    }
}
