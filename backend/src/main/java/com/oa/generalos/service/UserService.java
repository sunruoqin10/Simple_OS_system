package com.oa.generalos.service;

import com.oa.generalos.entity.User;
import com.oa.generalos.vo.LoginVO;
import com.oa.generalos.vo.UserVO;

public interface UserService {
    LoginVO login(String username, String password);

    User getUserById(Long id);

    UserVO getCurrentUser(Long userId);

    void updatePassword(Long userId, String oldPassword, String newPassword);
}
