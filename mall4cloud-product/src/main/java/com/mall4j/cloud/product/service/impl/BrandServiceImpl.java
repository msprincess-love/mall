package com.mall4j.cloud.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall4j.cloud.product.bo.CategoryBo;
import com.mall4j.cloud.product.dto.BrandDto;
import com.mall4j.cloud.product.dto.BrandVo;
import com.mall4j.cloud.product.mapper.BrandMapper;
import com.mall4j.cloud.product.mapper.CategoryBrandMapper;
import com.mall4j.cloud.product.mapper.SpuMapper;
import com.mall4j.cloud.product.model.Brand;
import com.mall4j.cloud.product.model.Category;
import com.mall4j.cloud.product.model.CategoryBrand;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.service.IBrandService;
import com.mall4j.cloud.product.service.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/23 17:54
 * @description mall
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {

    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryBrandMapper categoryBrandMapper;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private SpuMapper spuMapper;

    @Override
    public Page<Brand> pageBrands(Integer pageNum, Integer pageSize) {
        Page<Brand> pageInfo = new Page<>(pageNum, pageSize);
        return page(pageInfo);
    }

    @Override
    @Transactional
    public void addBrand(BrandDto brandDto) {
        Brand brand = new Brand();
        CategoryBrand categoryBrand = new CategoryBrand();
        BeanUtils.copyProperties(brandDto, brand);
        brandMapper.insert(brand);

        categoryBrand.setBrandId(brand.getBrandId());
        for (Long categoryId : brandDto.getCategoryIds()) {
            categoryBrand.setCategoryId(categoryId);
            categoryBrandMapper.insert(categoryBrand);
        }
    }

    @Override
    @Transactional
    @SuppressWarnings("all")
    public void deleteBrandById(Long brandId) {
        if (getById(brandId) == null)
            throw new RuntimeException("不存在id为" + brandId + "的数据");

        LambdaQueryWrapper<Spu> spuWrapper = new LambdaQueryWrapper<>();
        spuWrapper.eq(Spu::getBrandId, brandId);
        Integer spuCount = spuMapper.selectCount(spuWrapper);

        if (spuCount > 0)
            throw new RuntimeException("关联了相关spu平台商品信息，不能删除");

        LambdaQueryWrapper<CategoryBrand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryBrand::getBrandId, brandId);
        List<CategoryBrand> categoryBrands = categoryBrandMapper.selectList(wrapper);

        List<Long> ids = categoryBrands.stream().map(CategoryBrand::getId).collect(Collectors.toList());
        categoryBrandMapper.deleteBatchIds(ids);
        removeById(brandId);
    }

    @Override
    @Transactional
    public void updateBrand(BrandDto brandDto) {
        Brand brand = new Brand();
        BeanUtils.copyProperties(brandDto, brand);
        // 更新品牌表
        brandMapper.updateById(brand);

        // 更新品牌 - 分类的中间表
        LambdaQueryWrapper<CategoryBrand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryBrand::getBrandId, brand.getBrandId());
        List<CategoryBrand> categoryBrands = categoryBrandMapper.selectList(wrapper);
        List<Long> categoryIds = brandDto.getCategoryIds();

        int a = categoryBrands.size();
        int b = categoryIds.size();
        // 如果已经存在的该品牌的id所关联的分类（a） > 修改后的分类数量（b）, 那么就修改数量 b 的数据，删除 a - b 的数据
        if (a > b) {
            for (int i = 0; i < b; i ++) {
                categoryBrands.get(i).setCategoryId(categoryIds.get(i));
                categoryBrandMapper.updateById(categoryBrands.get(i));
            }
            // 截断此集合，该方法左闭右开
            List<CategoryBrand> newCategoryBrands = categoryBrands.subList(b, a);
            List<Long> ids = newCategoryBrands.stream().map(CategoryBrand::getId).collect(Collectors.toList());
            categoryBrandMapper.deleteBatchIds(ids);
        } // 如果已经存在的该品牌的id所关联的分类（a） < 修改后的分类数量（b）, 那么就修改数量 a 的数据，增加 b - a 的数据
        else if (a < b) {
            for (int i = 0; i < a; i ++) {
                categoryBrands.get(i).setCategoryId(categoryIds.get(i));
                categoryBrandMapper.updateById(categoryBrands.get(i));
            }
            for (int i = a; i < b; i ++) {
                CategoryBrand categoryBrand = new CategoryBrand();
                categoryBrand.setBrandId(brand.getBrandId());
                categoryBrand.setCategoryId(categoryIds.get(i));
                categoryBrandMapper.insert(categoryBrand);
            }
        } // 如果已经存在的该品牌的id所关联的分类（a）= 修改后的分类数量（b）, 那么就修改数量 a = b 的数据
        else {
            for (int i = 0; i < a; i ++) {
                categoryBrands.get(i).setCategoryId(categoryIds.get(i));
                categoryBrandMapper.updateById(categoryBrands.get(i));
            }
        }
    }

    @Override
    public void updateBrandStatus(Brand brand) {
        Brand now = brandMapper.selectById(brand.getBrandId());
        now.setStatus(brand.getStatus());
        updateById(now);
    }

    @Override
    public BrandVo getBrandById(Long brandId) {
        Brand brand = getById(brandId);
        BrandVo brandVo = new BrandVo();
        BeanUtils.copyProperties(brand, brandVo);

        LambdaQueryWrapper<CategoryBrand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryBrand::getBrandId, brand.getBrandId());
        List<CategoryBrand> categoryBrands = categoryBrandMapper.selectList(wrapper);

        List<Long> ids = categoryBrands.stream().map(CategoryBrand::getCategoryId).collect(Collectors.toList());

        List<Category> categories = categoryService.listByIds(ids);
        List<CategoryBo> categoryBos = categories.stream().map(category -> {
            CategoryBo categoryBo = new CategoryBo();
            categoryBo.setCategoryId(category.getCategoryId());
            categoryBo.setName(category.getName());
            categoryBo.setPathNames(getParentPathNameByCategory(category));
            return categoryBo;
        }).collect(Collectors.toList());

        brandVo.setCategories(categoryBos);

        return brandVo;
    }

    @Override
    @SuppressWarnings("all")
    public List<Brand> getBrandByCategoryId(Long categoryId, String name) {
        LambdaQueryWrapper<CategoryBrand> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryBrand::getCategoryId, categoryId);
        List<CategoryBrand> categoryBrands = categoryBrandMapper.selectList(wrapper);

        List<Long> ids = categoryBrands.stream().map(CategoryBrand::getBrandId).collect(Collectors.toList());

        if (name == null)
            return listByIds(ids);
        LambdaQueryWrapper<Brand> brandWrapper = new LambdaQueryWrapper<>();
        brandWrapper.in(Brand::getBrandId, ids);
        brandWrapper.like(Brand::getName, name);
        List<Brand> brands = list(brandWrapper);

        return brands;
    }

    private List<String> getParentPathNameByCategory(Category category) {
        List<String> list = new ArrayList<>(3);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getCategoryId, category.getParentId());
        Category secondLevelCategory = categoryService.getOne(wrapper);
        list.add(secondLevelCategory.getName());

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Category::getCategoryId, secondLevelCategory.getParentId());
        Category firstLevelCategory = categoryService.getOne(queryWrapper);
        list.add(firstLevelCategory.getName());

        return list;
    }
}
