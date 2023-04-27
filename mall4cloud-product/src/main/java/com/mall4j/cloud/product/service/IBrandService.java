package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.product.dto.BrandDto;
import com.mall4j.cloud.product.dto.BrandVo;
import com.mall4j.cloud.product.model.Brand;

import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/23 17:53
 * @description mall
 */
public interface IBrandService extends IService<Brand> {
    Page<Brand> pageBrands(Integer pageNum, Integer pageSize);

    void addBrand(BrandDto brandDto);


    void deleteBrandById(Long brandId);


    void updateBrand(BrandDto brandDto);

    void updateBrandStatus(Brand brand);

    BrandVo getBrandById(Long brandId);


    List<Brand> getBrandByCategoryId(Long categoryId, String name);

}
