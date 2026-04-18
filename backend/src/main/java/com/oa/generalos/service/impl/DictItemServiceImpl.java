package com.oa.generalos.service.impl;

import com.oa.generalos.entity.DictItem;
import com.oa.generalos.exception.BusinessException;
import com.oa.generalos.mapper.DictCategoryMapper;
import com.oa.generalos.mapper.DictItemMapper;
import com.oa.generalos.service.DictItemService;
import com.oa.generalos.vo.DictItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DictItemServiceImpl implements DictItemService {
    
    @Autowired
    private DictItemMapper itemMapper;
    
    @Autowired
    private DictCategoryMapper categoryMapper;
    
    @Override
    public List<DictItemVO> getAllItems() {
        return itemMapper.selectAll();
    }
    
    @Override
    public List<DictItemVO> getItemsByCategoryId(Long categoryId) {
        return itemMapper.selectByCategoryId(categoryId);
    }
    
    @Override
    public List<DictItemVO> getEnabledItemsByCategoryId(Long categoryId) {
        return itemMapper.selectEnabledByCategoryId(categoryId);
    }
    
    @Override
    public DictItemVO getItemById(Long id) {
        return itemMapper.selectById(id);
    }
    
    @Override
    @Transactional
    public void createItem(DictItem item) {
        if (categoryMapper.selectById(item.getCategoryId()) == null) {
            throw new BusinessException("分类不存在");
        }
        
        DictItemVO existing = itemMapper.selectByCategoryAndCode(item.getCategoryId(), item.getCode());
        if (existing != null) {
            throw new BusinessException("该分类下字典代码已存在");
        }
        
        if (item.getSortOrder() == null) {
            item.setSortOrder(0);
        }
        if (item.getStatus() == null) {
            item.setStatus(1);
        }
        
        itemMapper.insert(item);
    }
    
    @Override
    @Transactional
    public void updateItem(DictItem item) {
        DictItemVO existing = itemMapper.selectById(item.getId());
        if (existing == null) {
            throw new BusinessException("字典项不存在");
        }
        
        if (categoryMapper.selectById(item.getCategoryId()) == null) {
            throw new BusinessException("分类不存在");
        }
        
        if (!existing.getCategoryId().equals(item.getCategoryId()) || 
            !existing.getCode().equals(item.getCode())) {
            DictItemVO byCode = itemMapper.selectByCategoryAndCode(item.getCategoryId(), item.getCode());
            if (byCode != null) {
                throw new BusinessException("该分类下字典代码已存在");
            }
        }
        
        itemMapper.update(item);
    }
    
    @Override
    @Transactional
    public void deleteItem(Long id) {
        DictItemVO existing = itemMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("字典项不存在");
        }
        itemMapper.deleteById(id);
    }
    
    @Override
    @Transactional
    public void enableItem(Long id) {
        DictItemVO existing = itemMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("字典项不存在");
        }
        itemMapper.updateStatus(id, 1);
    }
    
    @Override
    @Transactional
    public void disableItem(Long id) {
        DictItemVO existing = itemMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException("字典项不存在");
        }
        itemMapper.updateStatus(id, 0);
    }
    
    @Override
    public List<DictItemVO> searchItems(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return itemMapper.selectAll();
        }
        return itemMapper.search(keyword.trim());
    }
}
