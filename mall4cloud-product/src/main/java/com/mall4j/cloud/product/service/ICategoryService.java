package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.product.dto.CategoryDto;
import com.mall4j.cloud.product.model.Category;
import com.mall4j.cloud.product.vo.CategoryVo;

import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/21 20:10
 * @description mall
 */
public interface ICategoryService extends IService<Category> {
    void addPlatFormOrShopCategory(CategoryDto categoryDto);

    List<CategoryVo> getPlatformCategoriesWithTree();

    List<CategoryVo> getShopCategoriesWithTree();

    Category getPlatformOrShopCategoryById(Long categoryId);


    void updatePlatformCategoryById(CategoryDto categoryDto);


    void deleteCategoryById(Long categoryId);


}
