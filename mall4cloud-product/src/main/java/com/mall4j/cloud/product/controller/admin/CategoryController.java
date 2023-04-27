package com.mall4j.cloud.product.controller.admin;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.CategoryDto;
import com.mall4j.cloud.product.model.Category;
import com.mall4j.cloud.product.service.ICategoryService;
import com.mall4j.cloud.product.vo.CategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/21 20:17
 * @description mall
 */
@RestController
@RequestMapping("/admin/category")
@Api(value = "平台分类接口")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/platform_categories")
    @ApiOperation(value = "平台商品分类信息", notes = "获取所有平台的分类信息（不分页）")
    public ServerResponseEntity<?> getPlatformCategories() {
        List<CategoryVo> data = categoryService.getPlatformCategoriesWithTree();
        return ServerResponseEntity.success(data);
    }

    @GetMapping("/shop_categories")
    @ApiOperation(value = "店铺商品分类信息", notes = "获取所有店铺的分类信息（不分页）")
    public ServerResponseEntity<?> getShopCategories() {
        List<CategoryVo> data = categoryService.getShopCategoriesWithTree();
        return ServerResponseEntity.success(data);
    }

    @PostMapping
    @ApiOperation(value = "添加商品信息")
    public ServerResponseEntity<?> addPlatFormCategory(@Validated @RequestBody CategoryDto categoryDto) {
        categoryService.addPlatFormOrShopCategory(categoryDto);
        return ServerResponseEntity.success();
    }

    @GetMapping
    @ApiOperation(value = "获取分类", notes = "根据id获取当前分类")
    public ServerResponseEntity<?> getPlatformCategoryById(@RequestParam("categoryId") Long categoryId) {
        Category category = categoryService.getPlatformOrShopCategoryById(categoryId);
        return ServerResponseEntity.success(category);
    }

    @PutMapping
    @ApiOperation(value = "修改分类")
    public ServerResponseEntity<?> updatePlatformCategoryById(@Validated @RequestBody CategoryDto categoryDto) {
        categoryService.updatePlatformCategoryById(categoryDto);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除分类")
    public ServerResponseEntity<?> deletePlatformCategoryById(@RequestParam("categoryId") Long categoryId) {
        categoryService.deleteCategoryById(categoryId);
        return ServerResponseEntity.success();
    }
}
