package com.mall4j.cloud.product.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall4j.cloud.common.response.ServerResponseEntity;
import com.mall4j.cloud.product.dto.AttrDto;
import com.mall4j.cloud.product.service.IAttrService;
import com.mall4j.cloud.product.vo.AttrVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/24 17:14
 * @description mall
 */
@RestController
@RequestMapping("/admin/attr")
@Api(value = "属性操作接口")
public class AttrController {

    @Autowired
    private IAttrService attrService;

    @GetMapping("/page")
    @ApiOperation(value = "分页获取属性的信息")
    public ServerResponseEntity<?> pageAttrVos(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "name", required = false) String name,
                                             @RequestParam(value = "attrType", required = false) Integer attrType){
        Page<AttrVo> attrPage = attrService.pageAttrVos(pageNum, pageSize, name, attrType);
        return ServerResponseEntity.success(attrPage);
    }

    @PostMapping
    @ApiOperation(value = "添加属性信息")
    public ServerResponseEntity<?> addBrand(@Validated @RequestBody AttrDto attrDto) {
        attrService.addBrand(attrDto);
        return ServerResponseEntity.success();
    }

    @GetMapping
    @ApiOperation(value = "回显属性数据")
    public ServerResponseEntity<?> getAttrById(@RequestParam("attrId") Long attrId) {
        AttrDto attrVo = attrService.getAttrById(attrId);
        return ServerResponseEntity.success(attrVo);
    }

    @DeleteMapping
    @ApiOperation(value = "删除属性信息")
    public ServerResponseEntity<?> deleteAttrById(@RequestParam("attrId") Long attrId) {
        attrService.deleteAttrById(attrId);
        return ServerResponseEntity.success();
    }

    @PutMapping
    @ApiOperation(value = "修改属性信息")
    public ServerResponseEntity<?> updateAttr(@Validated @RequestBody AttrDto attrDto) {
        attrService.updateAttr(attrDto);
        return ServerResponseEntity.success();
    }
}
