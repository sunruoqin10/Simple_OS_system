package com.oa.generalos.service;

import com.oa.generalos.entity.DictItem;
import com.oa.generalos.vo.DictItemVO;
import java.util.List;

public interface DictItemService {
    
    List<DictItemVO> getAllItems();
    
    List<DictItemVO> getItemsByCategoryId(Long categoryId);
    
    List<DictItemVO> getEnabledItemsByCategoryId(Long categoryId);
    
    DictItemVO getItemById(Long id);
    
    void createItem(DictItem item);
    
    void updateItem(DictItem item);
    
    void deleteItem(Long id);
    
    void enableItem(Long id);
    
    void disableItem(Long id);
    
    List<DictItemVO> searchItems(String keyword);
}
