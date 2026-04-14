package com.oa.generalos.service.impl;

import com.oa.generalos.entity.User;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.mapper.UserMapper;
import com.oa.generalos.service.UserService;
import com.oa.generalos.utils.JwtUtils;
import com.oa.generalos.vo.LoginVO;
import com.oa.generalos.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private static final int MAX_ERROR_COUNT = 5;
    private static final int LOCK_MINUTES = 30;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public LoginVO login(String username, String password) {
        User user = userMapper.findByUsername(username);

        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        if (user.isDisabled()) {
            throw new BusinessException(403, "账号已被禁用");
        }

        if (user.isLocked()) {
            throw new BusinessException(401, "账号已被锁定，请在" + LOCK_MINUTES + "分钟后重试");
        }

        if (!password.equals(user.getPassword())) {
            handlePasswordError(user);
            throw new BusinessException(401, "用户名或密码错误");
        }

        userMapper.updateErrorCount(user.getId(), 0);

        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUser(convertToUserVO(user));

        return loginVO;
    }

    private void handlePasswordError(User user) {
        int errorCount = user.getErrorCount() == null ? 0 : user.getErrorCount() + 1;
        userMapper.updateErrorCount(user.getId(), errorCount);

        if (errorCount >= MAX_ERROR_COUNT) {
            LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(LOCK_MINUTES);
            userMapper.updateLockedUntil(user.getId(), lockedUntil);
        }
    }

    @Override
    public User getUserById(Long id) {
        return userMapper.findById(id);
    }

    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        return convertToUserVO(user);
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        if (!oldPassword.equals(user.getPassword())) {
            throw new BusinessException(400, "原密码错误");
        }

        userMapper.updatePassword(userId, newPassword);
    }

    private UserVO convertToUserVO(User user) {
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setRealName(user.getRealName());
        vo.setRole(user.getRole());
        vo.setDepartmentId(user.getDepartmentId());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setMustChangePassword(user.getMustChangePassword());
        return vo;
    }
}
