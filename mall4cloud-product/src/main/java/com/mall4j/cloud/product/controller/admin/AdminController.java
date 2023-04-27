package com.mall4j.cloud.product.controller.admin;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.model.Brand;
import com.mall4j.cloud.product.service.IAttrService;
import com.mall4j.cloud.product.service.IBrandService;
import com.mall4j.cloud.product.vo.AttrVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/25 12:31
 * @description mall
 */
@RestController
@RequestMapping("/admin")

public class AdminController {
    @Autowired
    private IBrandService brandService;

    @Autowired
    private IAttrService attrService;

    @GetMapping("/brand/get_brand_by_category_id")
    @ApiOperation(value = "获取品牌信息", notes = "根据关联的分类id获取品牌")
    public ServerResponseEntity<?> getBrandByCategoryId(@RequestParam("categoryId") Long categoryId,
                                                        @RequestParam(value = "name", required = false) String name) {
        List<Brand> brands = brandService.getBrandByCategoryId(categoryId, name);
        return ServerResponseEntity.success(brands);
    }

    @GetMapping("/attr/get_attrs_by_category_id")
    @ApiOperation(value = "获取属性信息", notes = "根据关联的分类id获取属性")
    public ServerResponseEntity<?> getAttrByCategoryId(@RequestParam("categoryId") Long categoryId,
                                                        @RequestParam("attrType") Integer attrType) {
        List<AttrVo> attrValues = attrService.getAttrByCategoryId(categoryId, attrType);
        return ServerResponseEntity.success(attrValues);
    }

    @GetMapping("/attr/get_shop_attrs")
    public ServerResponseEntity<?> getShopAttrs() {
        List<AttrVo> attrValues = attrService.getShopAttrs();
        return ServerResponseEntity.success(attrValues);
    }
}
