package com.oa.generalos.service.impl;

import com.oa.generalos.entity.DictCategory;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.mapper.DictCategoryMapper;
import com.oa.generalos.mapper.DictItemMapper;
import com.oa.generalos.service.DictCategoryService;
import com.oa.generalos.vo.DictCategoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DictCategoryServiceImpl implements DictCategoryService {
    
    @Autowired
    private DictCategoryMapper categoryMapper;
    
    @Autowired
    private DictItemMapper itemMapper;
    
    @Override
    public List<DictCategoryVO> getAllCategories() {
        return categoryMapper.selectAll();
    }
    
    @Override
    public DictCategoryVO getCategoryById(Long id) {
        return categoryMapper.selectById(id);
    }
    
    @Override
    public DictCategoryVO getCategoryByCode(String code) {
        return categoryMapper.selectByCode(code);
    }
    
    @Override
    public void createCategory(DictCategory category) {
        DictCategoryVO existing = categoryMapper.selectByCode(category.getCode());
        if (existing != null) {
            throw new BusinessException("分类代码已存在");
        }
        if (category.getSortOrder() == null) {
            category.setSortOrder(0);
        }
        if (category.getStatus() == null) {
            category.setStatus(1);
        }
        categoryMapper.insert(category);
    }
    
    @Override
    public void updateCategory(DictCategory category) {
        DictCategoryVO existing = categoryMapper.selectById(category.getId());
        if (existing == null) {
            throw new BusinessException("分类不存在");
        }
        
        if (!existing.getCode().equals(category.getCode())) {
            DictCategoryVO byCode = categoryMapper.selectByCode(category.getCode());
            if (byCode != null) {
                throw new BusinessException("分类代码已存在");
            }
        }
        
        categoryMapper.update(category);
    }
    
    @Override
    public void deleteCategory(Long id) {
        DictCategoryVO existing = categoryMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("分类不存在");
        }
        
        int itemCount = itemMapper.countByCategoryId(id);
        if (itemCount > 0) {
            throw new BusinessException("该分类下存在字典项，无法删除");
        }
        
        int childCount = categoryMapper.countByParentId(id);
        if (childCount > 0) {
            throw new BusinessException("该分类下存在子分类，无法删除");
        }
        
        categoryMapper.deleteById(id);
    }
}
