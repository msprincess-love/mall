package com.mall4j.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.api.auth.bo.UserInfoInTokenBO;
import com.mall4j.cloud.common.security.AuthUserContext;
import com.mall4j.cloud.product.dto.CategoryDto;
import com.mall4j.cloud.product.mapper.*;
import com.mall4j.cloud.product.model.*;
import com.mall4j.cloud.product.service.ICategoryService;
import com.mall4j.cloud.product.vo.CategoryVo;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mall4j.cloud.product.constant.ProductConstant.CATEGORY_ROOT_PARENT_ID;
import static com.mall4j.cloud.product.constant.ProductConstant.PLATFORM_CATEGORY_VALUE;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/22 19:04
 * @description mall
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryBrandMapper categoryBrandMapper;
    @Autowired
    private AttrCategoryMapper attrCategoryMapper;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SkuMapper skuMapper;

    private static final ArrayList<Long> parentList = new ArrayList<>(3);
    private static final ArrayList<Long> childList = new ArrayList<>(3);

    @Override
    public void addPlatFormOrShopCategory(CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);

        UserInfoInTokenBO userInfoInTokenBO = AuthUserContext.get();
        if (userInfoInTokenBO != null && userInfoInTokenBO.getTenantId() != null) {
            category.setShopId(userInfoInTokenBO.getTenantId());
        }

        category.setPath("0");
        save(category);
    }

    @Override
    @SuppressWarnings("all")
    public List<CategoryVo> getPlatformCategoriesWithTree() {
        ArrayList<CategoryVo> categoryVos = new ArrayList<>();
        // 查出所有一级分类数据
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, CATEGORY_ROOT_PARENT_ID);
        wrapper.eq(Category::getShopId, PLATFORM_CATEGORY_VALUE);
        List<Category> oneLevelCategories = list(wrapper);

        for (Category oneLevelCategory : oneLevelCategories) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setCategoryId(oneLevelCategory.getCategoryId());
            categoryVo.setName(oneLevelCategory.getName());
            categoryVo.setChildren(getPlatformChildrensByParentId(oneLevelCategory.getCategoryId()));
            categoryVo.setLevel(oneLevelCategory.getLevel());
            categoryVo.setParentId(oneLevelCategory.getParentId());
            categoryVos.add(categoryVo);
        }
        return categoryVos;
    }

    @Override
    @SuppressWarnings("all")
    public List<CategoryVo> getShopCategoriesWithTree() {
        ArrayList<CategoryVo> categoryVos = new ArrayList<>();
        // 查出所有一级分类数据
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, CATEGORY_ROOT_PARENT_ID);
        wrapper.eq(Category::getShopId, AuthUserContext.get().getTenantId());
        List<Category> oneLevelCategories = list(wrapper);

        for (Category oneLevelCategory : oneLevelCategories) {
            CategoryVo categoryVo = new CategoryVo();
            categoryVo.setCategoryId(oneLevelCategory.getCategoryId());
            categoryVo.setName(oneLevelCategory.getName());
            categoryVo.setChildren(getShopChildrensByParentId(oneLevelCategory.getCategoryId()));
            categoryVo.setLevel(oneLevelCategory.getLevel());
            categoryVo.setParentId(oneLevelCategory.getParentId());
            categoryVos.add(categoryVo);
        }
        return categoryVos;
    }

    @Override
    public Category getPlatformOrShopCategoryById(Long categoryId) {
        if (getById(categoryId) == null)
            throw new RuntimeException("不存在id为" + categoryId + "的数据");
        return getById(categoryId);
    }

    @Override
    @SuppressWarnings("all")
    public void updatePlatformCategoryById(CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        StringBuilder sb = new StringBuilder();
        List<Long> parentIds = getAllParentIdByParentId(category.getParentId());
        List<Long> childIds = getAllChildIdByPrimaryKey(category.getCategoryId());

        // 组成 path {parentId} - {childId}
        if (AuthUserContext.get().getTenantId() == PLATFORM_CATEGORY_VALUE) {
            if (parentIds.size() == 0 && childIds.size() == 0) {
                sb.append(category.getCategoryId().toString());
            } else if (parentIds.size() > 0 && childIds.size() == 0) {
                for (int i = parentIds.size() - 1; i >= 0; i--)
                    sb.append(parentIds.get(i).toString()).append("-");
                sb.append(category.getCategoryId().toString());
            } else if (parentIds.size() == 0 && childIds.size() > 0) {
                sb.append(category.getCategoryId().toString()).append("-");
                for (Long childId : childIds) {
                    if (childIds.indexOf(childId) == childIds.size() - 1)
                        sb.append(childId.toString());
                    else
                        sb.append(childId.toString()).append("-");
                }
            } else {
                for (int i = parentIds.size() - 1; i >= 0; i--)
                    sb.append(parentIds.get(i).toString()).append("-");

                sb.append(category.getCategoryId().toString()).append("-");
                sb.append(childIds.get(0).toString());
            }
            category.setPath(sb.toString());

            updateById(category);
        } else {
            if (parentIds.size() == 0 && childIds.size() == 0)
                sb.append(category.getCategoryId().toString());
            else if (parentIds.size() > 0 && childIds.size() == 0) {
                sb.append(parentIds.get(0).toString()).append("-");
                sb.append(category.getCategoryId().toString());
            } else if (parentIds.size() == 0 && childIds.size() > 0) {
                sb.append(category.getCategoryId().toString()).append("-");
                sb.append(childIds.get(0).toString());
            }
        }
    }

    @Override
    @SuppressWarnings("all")
    public void deleteCategoryById(Long categoryId) {
        Category category = getById(categoryId);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, categoryId);
        wrapper.eq(Category::getShopId, AuthUserContext.get().getTenantId());
        int count = count(wrapper);

        if (count > 0)
            throw new RuntimeException("存在孩子节点，不能删除");

        if (category.getShopId() == PLATFORM_CATEGORY_VALUE) {
            LambdaQueryWrapper<Spu> spuWrapper = new LambdaQueryWrapper<>();
            spuWrapper.eq(Spu::getCategoryId, categoryId);
            Integer spuCount = spuMapper.selectCount(spuWrapper);

            if (spuCount > 0)
                throw new RuntimeException("关联了相关spu平台商品信息，不能删除");
        } else {
            LambdaQueryWrapper<Spu> spuWrapper = new LambdaQueryWrapper<>();
            spuWrapper.eq(Spu::getShopCategoryId, categoryId);
            Integer spuCount = spuMapper.selectCount(spuWrapper);

            if (spuCount > 0)
                throw new RuntimeException("关联了相关spu销售商品信息，不能删除");
        }


        removeById(categoryId);

        LambdaQueryWrapper<CategoryBrand> categoryBrandWrapper = new LambdaQueryWrapper<>();
        categoryBrandWrapper.eq(CategoryBrand::getCategoryId, categoryId);
        List<CategoryBrand> categoryBrands = categoryBrandMapper.selectList(categoryBrandWrapper);
        List<Long> ids = categoryBrands.stream().map(CategoryBrand::getId).collect(Collectors.toList());

        categoryBrandMapper.deleteBatchIds(ids);

        LambdaQueryWrapper<AttrCategory> attrCategoryWrapper = new LambdaQueryWrapper<>();
        attrCategoryWrapper.eq(AttrCategory::getCategoryId, categoryId);
        List<AttrCategory> attrCategories = attrCategoryMapper.selectList(attrCategoryWrapper);
        List<Long> attrIds = attrCategories.stream().map(AttrCategory::getAttrCategoryId).collect(Collectors.toList());

        attrCategoryMapper.deleteBatchIds(attrIds);
    }

    @SuppressWarnings("all")
    private List<CategoryVo> getPlatformChildrensByParentId(Long parentId) {
        List<CategoryVo> result = new ArrayList<>();
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, parentId);
        wrapper.eq(Category::getShopId, PLATFORM_CATEGORY_VALUE);
        List<Category> categories = list(wrapper);

        if (categories != null && categories.size() > 0) {
            for (Category category : categories) {
                CategoryVo categoryVo = new CategoryVo();
                categoryVo.setCategoryId(category.getCategoryId());
                categoryVo.setName(category.getName());
                categoryVo.setChildren(getPlatformChildrensByParentId(category.getCategoryId()));
                categoryVo.setLevel(category.getLevel());
                categoryVo.setParentId(parentId);
                result.add(categoryVo);
            }
        }
        return result;
    }

    @SuppressWarnings("all")
    private List<CategoryVo> getShopChildrensByParentId(Long parentId) {
        List<CategoryVo> result = new ArrayList<>();
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, parentId);
        wrapper.eq(Category::getShopId, AuthUserContext.get().getTenantId());
        List<Category> categories = list(wrapper);

        if (categories != null && categories.size() > 0) {
            for (Category category : categories) {
                CategoryVo categoryVo = new CategoryVo();
                categoryVo.setCategoryId(category.getCategoryId());
                categoryVo.setName(category.getName());
                categoryVo.setChildren(getPlatformChildrensByParentId(category.getCategoryId()));
                categoryVo.setLevel(category.getLevel());
                categoryVo.setParentId(parentId);
                result.add(categoryVo);
            }
        }
        return result;
    }

    public List<Long> getAllParentIdByParentId(Long parentId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getCategoryId, parentId);
        wrapper.eq(Category::getShopId, AuthUserContext.get().getTenantId());
        Category categoryParent = getOne(wrapper);

        if (categoryParent != null) {
            parentList.add(categoryParent.getCategoryId());
            this.getAllParentIdByParentId(categoryParent.getParentId());
        }
        return parentList;
    }

    public List<Long> getAllChildIdByPrimaryKey(Long categoryId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, categoryId);
        wrapper.eq(Category::getShopId, AuthUserContext.get().getTenantId());
        Category categoryChild = getOne(wrapper);

        if (categoryChild != null) {
            childList.add(categoryChild.getCategoryId());
            this.getAllChildIdByPrimaryKey(categoryChild.getCategoryId());
        }
        return childList;
    }
}
