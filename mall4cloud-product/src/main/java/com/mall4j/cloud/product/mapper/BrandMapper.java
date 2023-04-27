package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.product.dto.BrandDto;
import com.mall4j.cloud.product.model.Brand;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/23 17:53
 * @description mall
 */
@Mapper
public interface BrandMapper extends BaseMapper<Brand> {

}
