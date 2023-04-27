package com.mall4j.cloud.product.controller.admin;

import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.SpuDto;
import com.mall4j.cloud.product.service.ISpuService;
import com.mall4j.cloud.product.vo.CategoryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/26 11:15
 * @description mall
 */
@RestController
@RequestMapping("/admin/spu")
public class SpuController {

    @Autowired
    private ISpuService spuService;

    @PostMapping
    @ApiOperation(value = "新增spu的属性")
    public ServerResponseEntity<?> addSpuAndSkuAndAttr(@Validated @RequestBody SpuDto spuDto) {
        spuService.addSpuAndSkuAndAttr(spuDto);
        return ServerResponseEntity.success();
    }

    @DeleteMapping
    @ApiOperation(value = "删除spu的相关属性")
    public ServerResponseEntity<?> deleteSpuAndSkuAndAttrById(@RequestParam("spuId") Long spuId) {
        spuService.deleteSpuAndSkuAndAttrById(spuId);
        return ServerResponseEntity.success();
    }

    @GetMapping
    @ApiOperation(value = "获取spu的相关属性", notes = "根据id获取相关信息")
    public ServerResponseEntity<?> getSpuAndSkuAndAttrById(@RequestParam("spuId") Long spuId) {
        SpuDto spuDto = spuService.getSpuAndSkuAndAttrById(spuId);
        return ServerResponseEntity.success(spuDto);
    }

    @PutMapping
    @ApiOperation(value = "修改spu相关属性")
    public ServerResponseEntity<?> updateSpuAndSkuAndAttr(SpuDto spuDto) {
        spuService.updateSpuAndSkuAndAttr(spuDto);
        return ServerResponseEntity.success();
    }
}
