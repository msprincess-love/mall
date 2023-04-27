package com.mall4j.cloud.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall4j.cloud.product.dto.SearchProductDto;
import com.mall4j.cloud.product.dto.SpuDto;
import com.mall4j.cloud.product.model.Spu;
import com.mall4j.cloud.product.vo.SpuAndSkuVo;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/25 15:54
 * @description mall
 */
public interface ISpuService extends IService<Spu> {
    void addSpuAndSkuAndAttr(SpuDto spuDto);

    void deleteSpuAndSkuAndAttrById(Long spuId);

    SpuDto getSpuAndSkuAndAttrById(Long spuId);

    void updateSpuAndSkuAndAttr(SpuDto spuDto);



//    Page<SpuAndSkuVo> pageAllProducts(Integer pageSize, Integer pageNum, SearchProductDto searchProductDto);


}
