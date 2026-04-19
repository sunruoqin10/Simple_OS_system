package com.oa.generalos.mapper;

import com.oa.generalos.entity.Role;
import com.oa.generalos.vo.RoleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoleMapper {
    List<RoleVO> findAll();

    List<RoleVO> findByStatus(@Param("status") Integer status);

    RoleVO findById(@Param("id") Long id);

    RoleVO findByCode(@Param("code") String code);

    int insert(Role role);

    int update(Role role);

    int deleteById(@Param("id") Long id);

    int countByCode(@Param("code") String code);

    int countUserByRoleId(@Param("roleId") Long roleId);
}