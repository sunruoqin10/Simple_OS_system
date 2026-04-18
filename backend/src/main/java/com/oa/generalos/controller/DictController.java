package com.oa.generalos.controller;

import com.oa.generalos.common.Result;
import com.oa.generalos.entity.DictCategory;
import com.oa.generalos.entity.DictItem;
import com.oa.generalos.service.DictCategoryService;
import com.oa.generalos.service.DictItemService;
import com.oa.generalos.vo.DictCategoryVO;
import com.oa.generalos.vo.DictItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/dict")
public class DictController {
    
    @Autowired
    private DictCategoryService categoryService;
    
    @Autowired
    private DictItemService itemService;
    
    @GetMapping("/category/list")
    public Result<List<DictCategoryVO>> getAllCategories() {
        List<DictCategoryVO> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }
    
    @GetMapping("/category/{id}")
    public Result<DictCategoryVO> getCategoryById(@PathVariable Long id) {
        DictCategoryVO category = categoryService.getCategoryById(id);
        return Result.success(category);
    }
    
    @GetMapping("/category/code/{code}")
    public Result<DictCategoryVO> getCategoryByCode(@PathVariable String code) {
        DictCategoryVO category = categoryService.getCategoryByCode(code);
        return Result.success(category);
    }
    
    @PostMapping("/category/create")
    public Result<Void> createCategory(@RequestBody DictCategory category) {
        categoryService.createCategory(category);
        return Result.success(null);
    }
    
    @PutMapping("/category/update")
    public Result<Void> updateCategory(@RequestBody DictCategory category) {
        categoryService.updateCategory(category);
        return Result.success(null);
    }
    
    @DeleteMapping("/category/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success(null);
    }
    
    @GetMapping("/item/list")
    public Result<List<DictItemVO>> getAllItems() {
        List<DictItemVO> items = itemService.getAllItems();
        return Result.success(items);
    }
    
    @GetMapping("/item/category/{categoryId}")
    public Result<List<DictItemVO>> getItemsByCategory(@PathVariable Long categoryId) {
        List<DictItemVO> items = itemService.getItemsByCategoryId(categoryId);
        return Result.success(items);
    }
    
    @GetMapping("/item/category/{categoryId}/enabled")
    public Result<List<DictItemVO>> getEnabledItemsByCategory(@PathVariable Long categoryId) {
        List<DictItemVO> items = itemService.getEnabledItemsByCategoryId(categoryId);
        return Result.success(items);
    }
    
    @GetMapping("/item/{id}")
    public Result<DictItemVO> getItemById(@PathVariable Long id) {
        DictItemVO item = itemService.getItemById(id);
        return Result.success(item);
    }
    
    @PostMapping("/item/create")
    public Result<Void> createItem(@RequestBody DictItem item) {
        itemService.createItem(item);
        return Result.success(null);
    }
    
    @PutMapping("/item/update")
    public Result<Void> updateItem(@RequestBody DictItem item) {
        itemService.updateItem(item);
        return Result.success(null);
    }
    
    @DeleteMapping("/item/{id}")
    public Result<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return Result.success(null);
    }
    
    @PutMapping("/item/{id}/enable")
    public Result<Void> enableItem(@PathVariable Long id) {
        itemService.enableItem(id);
        return Result.success(null);
    }
    
    @PutMapping("/item/{id}/disable")
    public Result<Void> disableItem(@PathVariable Long id) {
        itemService.disableItem(id);
        return Result.success(null);
    }
    
    @GetMapping("/item/search")
    public Result<List<DictItemVO>> searchItems(@RequestParam String keyword) {
        List<DictItemVO> items = itemService.searchItems(keyword);
        return Result.success(items);
    }
}
