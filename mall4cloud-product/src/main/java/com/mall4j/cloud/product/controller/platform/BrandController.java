package com.mall4j.cloud.product.controller.platform;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.BrandDto;
import com.mall4j.cloud.product.dto.BrandVo;
import com.mall4j.cloud.product.model.Brand;
import com.mall4j.cloud.product.service.IBrandService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/23 17:46
 * @description mall
 */
@RestController
@RequestMapping("/platform/brand")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @GetMapping("/page")
    @ApiOperation(value = "分页获取商品的信息")
    public ServerResponseEntity<?> pageBrands(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                           @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<Brand> brandPage = brandService.pageBrands(pageNum, pageSize);
        return ServerResponseEntity.success(brandPage);
    }

    @PostMapping
    @ApiOperation(value = "添加商品信息")
    public ServerResponseEntity<?> addBrand(@Validated @RequestBody BrandDto brandDto) {
        brandService.addBrand(brandDto);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除品牌信息")
    public ServerResponseEntity<?> deleteBrandById(@RequestParam("brandId") Long brandId) {
        brandService.deleteBrandById(brandId);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "修改品牌")
    public ServerResponseEntity<?> updateBrand(@Validated @RequestBody BrandDto brandDto) {
        brandService.updateBrand(brandDto);
        return ServerResponseEntity.success();
    }

    @PutMapping("/update_brand_status")
    @ApiOperation(value = "上架品牌", notes = "默认是不上架")
    public ServerResponseEntity<?> updateBrandStatus(@RequestBody Brand brand) {
        brandService.updateBrandStatus(brand);
        return ServerResponseEntity.success();
    }

    @GetMapping
    @ApiOperation(value = "回显品牌数据")
    public ServerResponseEntity<?> getBrandById(@RequestParam("brandId") Long brandId) {
        BrandVo brandVo = brandService.getBrandById(brandId);
        return ServerResponseEntity.success(brandVo);
    }


}
