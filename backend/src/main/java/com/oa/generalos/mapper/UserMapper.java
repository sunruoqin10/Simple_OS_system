package com.oa.generalos.mapper;

import com.oa.generalos.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {
    User findByUsername(@Param("username") String username);

    User findById(@Param("id") Long id);

    List<User> findAll();

    int insert(User user);

    int update(User user);

    int deleteById(@Param("id") Long id);

    int count();

    int updatePassword(@Param("id") Long id, @Param("password") String password);

    int updateErrorCount(@Param("id") Long id, @Param("errorCount") Integer errorCount);

    int updateLockedUntil(@Param("id") Long id, @Param("lockedUntil") java.time.LocalDateTime lockedUntil);
}
