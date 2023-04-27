package com.mall4j.cloud.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall4j.cloud.product.model.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zhstart_bytedance
 * @version 1.0
 * @date 2023/4/21 20:09
 * @description mall
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
