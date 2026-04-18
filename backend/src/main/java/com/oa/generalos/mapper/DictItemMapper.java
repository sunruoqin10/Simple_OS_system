package com.oa.generalos.mapper;

import com.oa.generalos.entity.DictItem;
import com.oa.generalos.vo.DictItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface DictItemMapper {
    
    List<DictItemVO> selectAll();
    
    List<DictItemVO> selectByCategoryId(@Param("categoryId") Long categoryId);
    
    List<DictItemVO> selectEnabledByCategoryId(@Param("categoryId") Long categoryId);
    
    DictItemVO selectById(@Param("id") Long id);
    
    DictItemVO selectByCategoryAndCode(@Param("categoryId") Long categoryId, @Param("code") String code);
    
    int insert(DictItem item);
    
    int update(DictItem item);
    
    int deleteById(@Param("id") Long id);
    
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);
    
    int countByCategoryId(@Param("categoryId") Long categoryId);
    
    List<DictItemVO> search(@Param("keyword") String keyword);
}
