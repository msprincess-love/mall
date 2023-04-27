//package com.mall4j.cloud.product.controller.admin;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.mall4j.cloud.common.response.ServerResponseEntity;
//import com.mall4j.cloud.product.dto.SearchProductDto;
//import com.mall4j.cloud.product.service.ISpuService;
//import com.mall4j.cloud.product.vo.SpuAndSkuVo;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author zhstart_bytedance
// * @version 1.0
// * @date 2023/4/26 16:51
// * @description mall
// */
//@RestController
//@RequestMapping("/m/search")
//public class ProductController {
//
//    @Autowired
//    private ISpuService spuService;
//
//    @GetMapping("/page")
//    @ApiOperation(value = "获取商品数据")
//    public ServerResponseEntity<?> pageAllProducts(@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
//                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
//                                                   SearchProductDto searchProductDto) {
//        Page<SpuAndSkuVo> spuAndSkuVoPage = spuService.pageAllProducts(pageSize, pageNum, searchProductDto);
//        return ServerResponseEntity.success(spuAndSkuVoPage);
//    }
//}
