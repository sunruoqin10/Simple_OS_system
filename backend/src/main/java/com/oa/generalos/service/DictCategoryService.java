package com.oa.generalos.service;

import com.oa.generalos.entity.DictCategory;
import com.oa.generalos.vo.DictCategoryVO;
import java.util.List;

public interface DictCategoryService {
    
    List<DictCategoryVO> getAllCategories();
    
    DictCategoryVO getCategoryById(Long id);
    
    DictCategoryVO getCategoryByCode(String code);
    
    void createCategory(DictCategory category);
    
    void updateCategory(DictCategory category);
    
    void deleteCategory(Long id);
}
