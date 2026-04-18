package com.oa.generalos.service;

import com.oa.generalos.entity.User;
import com.oa.generalos.vo.LoginVO;
import com.oa.generalos.vo.UserVO;
import java.util.List;

public interface UserService {
    LoginVO login(String username, String password);

    User getUserById(Long id);

    UserVO getCurrentUser(Long userId);

    void updatePassword(Long userId, String oldPassword, String newPassword);

    List<UserVO> getAllUsers();

    void createUser(String username, String password, String realName, String role, Long departmentId, String email, String phone);

    void updateUser(Long id, String realName, String role, Long departmentId, String email, String phone, Integer status);

    void deleteUser(Long id);

    int getUserCount();
}
