package com.oa.generalos.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserRoleMapper {
    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);

    List<Long> findPermissionIdsByUserId(@Param("userId") Long userId);

    List<String> findPermissionCodesByUserId(@Param("userId") Long userId);

    int insert(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int deleteByUserId(@Param("userId") Long userId);

    int deleteByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int countByRoleId(@Param("roleId") Long roleId);
}