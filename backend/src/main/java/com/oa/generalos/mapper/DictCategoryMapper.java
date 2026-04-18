package com.oa.generalos.mapper;

import com.oa.generalos.entity.DictCategory;
import com.oa.generalos.vo.DictCategoryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DictCategoryMapper {
    
    List<DictCategoryVO> selectAll();
    
    DictCategoryVO selectById(@Param("id") Long id);
    
    DictCategoryVO selectByCode(@Param("code") String code);
    
    int insert(DictCategory category);
    
    int update(DictCategory category);
    
    int deleteById(@Param("id") Long id);
    
    int countByParentId(@Param("parentId") Long parentId);
}
