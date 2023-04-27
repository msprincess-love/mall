package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.product.model.SkuStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/26 18:45
 * @description mall
 */
@Mapper
public interface SkuStockMapper extends BaseMapper<SkuStock> {
}
